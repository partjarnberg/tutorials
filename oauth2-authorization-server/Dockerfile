FROM gradle:6.6.1-jdk14 as BASE
WORKDIR /authorization-server
COPY ./ .
RUN gradle build

FROM openjdk:14
EXPOSE 8080
COPY --from=BASE /authorization-server/build/libs/authorization-server*.jar /app.jar
CMD java -jar -Xmx2096m  /app.jar