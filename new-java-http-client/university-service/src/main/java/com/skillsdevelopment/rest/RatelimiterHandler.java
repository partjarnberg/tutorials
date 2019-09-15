package com.skillsdevelopment.rest;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.skillsdevelopment.rest.representation.ErrorRepresentation;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class RatelimiterHandler implements HttpHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RatelimiterHandler.class);

    // The maximum number of requests you're permitted to make per hour.
    private static final HttpString HTTP_HEADER_RATE_LIMIT = HttpString.tryFromString("X-RateLimit-Limit");
    // The number of requests remaining in the current rate limit window.
    private static final HttpString HTTP_HEADER_RATE_REMAINING = HttpString.tryFromString("X-RateLimit-Remaining");
    // The time at which the current rate limit window resets in UTC epoch seconds.
    private static final HttpString HTTP_HEADER_RATE_RESET = HttpString.tryFromString("X-RateLimit-Reset");

    private final RequestUtil requestUtil;
    private final HttpHandler next;
    private final static LoadingCache<InetAddress, RateLimit> LIMITERS = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build(new CacheLoader<>() {
                @Override
                public RateLimit load(final InetAddress ignore) {
                    LOGGER.debug("Adding rate limit for {}", ignore);
                    return new RateLimit();
                }
            });

    public RatelimiterHandler(final RequestUtil requestUtil, final HttpHandler next) {
        this.requestUtil = requestUtil;
        this.next = next;

    }

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        final RateLimit rateLimit = LIMITERS.get(extractClientIpAddress(exchange));
        final boolean aquired = rateLimit.aquire();

        exchange.getResponseHeaders().put(HTTP_HEADER_RATE_LIMIT, rateLimit.getLimit());
        exchange.getResponseHeaders().put(HTTP_HEADER_RATE_REMAINING, rateLimit.getRemaining());
        exchange.getResponseHeaders().put(HTTP_HEADER_RATE_RESET, rateLimit.getReset());

        if(aquired) {
            next.handleRequest(exchange);
        } else {
            requestUtil.sendTooManyRequests(exchange, new ErrorRepresentation("Too many requests."));
        }
    }

    private InetAddress extractClientIpAddress(final HttpServerExchange exchange) {
        return exchange.getSourceAddress().getAddress();
    }

    private static class RateLimit {
        private static final Long LIMIT = 3600L;
        private final Instant reset;
        private AtomicLong remaining;

        RateLimit() {
            reset = Instant.now().plus(1, ChronoUnit.HOURS);
            remaining = new AtomicLong(LIMIT);
        }

        Long getLimit() {
            return LIMIT;
        }

        Long getReset() {
            return reset.getEpochSecond();
        }

        Long getRemaining() {
            return remaining.longValue();
        }

        boolean aquire() {
            if(remaining.get() <= 0)
                return false;
            return remaining.decrementAndGet() >= 0;
        }
    }
}
