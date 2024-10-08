# Uses openjdk 17
FROM openjdk:17

# Copy the source code to the container
COPY . /usr/src/myapp

# Set the working directory
WORKDIR /usr/src/myapp

# Run the build script
RUN ./mvnw clean install -DskipTests

# Run the startup script
CMD ["java", "-jar", "target/*.jar"]
