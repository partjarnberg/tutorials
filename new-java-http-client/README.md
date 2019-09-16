# New Java HTTP client

## Scenario
Develop the next generation school system.

You are about to develop a web service letting a student register for THE University, apply for a couple of courses for the upcoming semester. 
Furthermore, the student should have the possibility to extract a list of course literature to plan the semester.

Requirements
 * Develop a backend aggregating the two services _university-service_ and _book-service_. Transform and possibly add data and expose it all through a REST API serving JSON.
 * Non-blocking 
 * Asynchronous - the backend should be written in an asynchronous manner

__Bonus:__ _add functionality to make an economical forecast when book must be bought based on course start date and the current price of the book._    

## Implementation
[![java12](https://img.shields.io/badge/java-12-blue.svg?longCache=true&style=for-the-badge)](https://docs.oracle.com/en/java/javase/12/) 
[![http client](https://img.shields.io/badge/http--client-http/2-red.svg?longCache=true&style=for-the-badge)](https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/package-summary.html)

## Technologies
The following technologies have been used.
* [Undertow](http://undertow.io/)
  - Default web server of JBoss Wildfly (application server)
  - Supports non-blocking IO (NIO) by default
* [New Java HTTP client](https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/package-summary.html) 
  - The HTTP Client module was bundled as an incubator module in Java 9. Since Java 11 it has been adopted and found in module **java.net.http**. The "new" http client supports HTTP/2 with backward compatibility still facilitating HTTP/1.1.
  - A flyby of the new http client by [Baeldung](https://www.baeldung.com/java-9-http-client)
* Streams and CompletableFutures as of Java 8
* [Jackson (fasterxml)](https://github.com/FasterXML/jackson-core)
  - For parsing and serializing JSON
* [SLF4J](https://www.slf4j.org/) (logback)
  - Logging specification and framework
* [Guava](https://github.com/google/guava)'s cache
* [Commons CLI](https://commons.apache.org/proper/commons-cli/)
  - To support _httpPort_ argument when running the application
* JUnit
  - Java unit test framework

## Build and run
### Build
./gradlew clean build shadowJar

### Run
java -jar build/libs/new-java-http-client-1.0-SNAPSHOT.jar -httpPort 8080

## Use docker-compose
Run __docker-compose up__ to get the entire system up and running.
 - book-service
 - university-service
 - new-java-http-client  
