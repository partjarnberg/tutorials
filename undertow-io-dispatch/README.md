Quick and dirty example how to get Undertow up and running making use of the process of dispatching from I/O thread to be able to handle as many requests as possible. 

[![java12](https://img.shields.io/badge/java-12-blue.svg?longCache=true&style=for-the-badge)](https://docs.oracle.com/en/java/javase/12/)
[![undertow](https://img.shields.io/badge/undertow-2-red.svg?longCache=true&style=for-the-badge)](http://undertow.io/)  

## Technologies
The following technologies have been used.
* [Undertow](http://undertow.io/)
  - Default web server of JBoss Wildfly (application server)
  - Supports non-blocking IO (NIO) by default
* [Jackson (fasterxml)](https://github.com/FasterXML/jackson-core)
  - For parsing and serializing JSON
* [SLF4J](https://www.slf4j.org/) (logback)
  - Logging specification and framework
* [Commonc CLI](https://commons.apache.org/proper/commons-cli/)
  - To support _httpPort_ argument when running the application

## Build and run
### Build
./gradlew clean build shadowJar

### Run
java -jar build/libs/undertow-io-dispatch-1.0-SNAPSHOT.jar -httpPort 8080