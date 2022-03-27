FROM openjdk:11
VOLUME /tmp
ARG JAR_FILE=/build/libs/gateway-1.0-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
ENTRYPOINT echo "LAUNCHING!!!" \
    && java -jar app.jar