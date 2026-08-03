FROM amazoncorretto:17-al2023-headless
ENV SPRING_PROFILE_ACTIVE=prod
WORKDIR /app
COPY build/libs/aws-profile-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]