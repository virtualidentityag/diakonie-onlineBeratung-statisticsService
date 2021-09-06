FROM openjdk:11.0.10-jre-slim-buster
VOLUME ["/tmp","/log"]
EXPOSE 8080
ARG JAR_FILE
COPY ./StatisticsService.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
