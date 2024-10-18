# Stage 1: Build the application
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and download the project dependencies
COPY pom.xml .

# Download dependencies (this will cache dependencies if pom.xml hasn't changed)
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create a lightweight image to run the Spring Boot application
FROM eclipse-temurin:17-jre

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage to the current image
COPY --from=build /app/target/helloworld-0.0.1-SNAPSHOT.jar /app/helloworld.jar

# Expose the port that the Spring Boot application will run on
EXPOSE 8080

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/helloworld.jar"]
