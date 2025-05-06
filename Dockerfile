# Use official OpenJDK image
FROM openjdk:17

# Set the working directory
WORKDIR /app

# Copy project files
COPY . .

# Build the project
RUN ./mvnw clean install -DskipTests

# Run the app
CMD ["java", "-jar", "target/StudyHub-0.0.1-SNAPSHOT.jar"]