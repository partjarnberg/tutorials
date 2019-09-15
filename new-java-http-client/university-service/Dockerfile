FROM gradle:5.6.2-jdk12
COPY --chown=gradle:gradle . /
WORKDIR /
RUN ./gradlew shadowJar