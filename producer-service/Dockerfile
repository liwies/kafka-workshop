# Use a lightweight base image with a JRE
FROM openjdk:17-jdk-slim

# Copy the JAR file into the container
COPY target/*.jar producer-service-1.0.0.jar

# Expose the port your Spring Boot app is running on (usually 8080)
EXPOSE 8084

# Set the command to run when the container starts
ENTRYPOINT ["java", "-jar", "/producer-service-1.0.0.jar"]