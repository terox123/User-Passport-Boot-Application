FROM openjdk:17-jdk-slim-buster
WORKDIR /app
COPY target/UserPassportBoot-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]