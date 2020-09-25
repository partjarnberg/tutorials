package buzz.programmers.auth.web.authorization.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;
import buzz.programmers.auth.server.UserService;
import buzz.programmers.auth.server.model.Account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.of;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@Configuration
@EnableConfigurationProperties(UserService.class)
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Value("${security.oauth2.clientId}")
    private String clientId;
    @Value("${security.oauth2.clientSecret}")
    private String clientSecret;
    @Value("#{'${security.oauth2.grantTypes}'.split(',')}")
    private List<String> grantTypes;
    @Value("#{'${security.oauth2.scopes}'.split(',')}")
    private List<String> scopes;
    @Value("#{'${security.oauth2.redirectUris}'.split(',')}")
    private List<String> redirectUris;
    @Value("${security.jwt.signingKey}")
    private String signingKey;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public OAuth2AuthorizationServerConfig(final AuthenticationManager authenticationManager,
                                           final PasswordEncoder passwordEncoder,
                                           final UserService userService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient(clientId)
                .secret(passwordEncoder.encode(clientSecret))
                .authorizedGrantTypes(grantTypes.toArray(new String[0]))
                .scopes(scopes.toArray(new String[0]))
                .autoApprove(true)
                .accessTokenValiditySeconds((int) of(MINUTES).toSeconds(15))
                .refreshTokenValiditySeconds((int) of(HOURS).toSeconds(24))
                .redirectUris(redirectUris.toArray(new String[0]));
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(
                List.of(tokenEnhancer(), accessTokenConverter()));

        endpoints.tokenStore(tokenStore())
                .reuseRefreshTokens(false)
                .accessTokenConverter(accessTokenConverter())
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager)
                .userDetailsService(userService)
                .addInterceptor(new HandlerInterceptorAdapter() {
                    // This is a fix for invalidating the session as soon as the authorization code is provided
                    // in the 'authorization code grant flow'. This is due to a limitation in Spring Security.
                    // See more at https://github.com/spring-projects/spring-security-oauth/issues/140#issuecomment-324002674
                    @Override
                    public void postHandle(final HttpServletRequest request, final HttpServletResponse response,
                                           final Object handler, final ModelAndView modelAndView) {
                        if (modelAndView != null && modelAndView.getView() instanceof RedirectView) {
                            fromHttpUrl(requireNonNull(((RedirectView) modelAndView.getView()).getUrl())).build()
                                    .getQueryParams().keySet().stream().filter(Set.of("code", "error")::contains)
                                    .findFirst().flatMap(ignored -> ofNullable(request.getSession(false)))
                                    .ifPresent(HttpSession::invalidate);
                        }
                    }
                });
    }

    // Fix to add CORS filter to token endpoint on Authorization server
    // see more at https://github.com/spring-projects/spring-security-oauth/issues/1512#issuecomment-478758610
    @Override
    public void configure(final AuthorizationServerSecurityConfigurer security) {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.applyPermitDefaultValues();

        source.registerCorsConfiguration("/oauth/token", config);
        final CorsFilter filter = new CorsFilter(source);
        security.addTokenEndpointAuthenticationFilter(filter);
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            final Account user = userService.loadAccountByUsername(authentication.getName()).orElseThrow();
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(
                    Map.of("first_name", user.getFirstName(),
                            "last_name", user.getLastName()));
            return accessToken;
        };
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(signingKey);
        return converter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    @Primary
    //Making this primary to avoid any accidental duplication with another token service instance of the same name
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }
}
