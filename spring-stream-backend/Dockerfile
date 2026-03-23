# Use an official Maven image as the base image for building
FROM maven:3.9.4-openjdk-21-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and download dependencies (for better caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "app.jar"]