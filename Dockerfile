FROM eclipse-temurin:17-jdk-alpine

# copy jar into image
RUN mkdir -p /app
WORKDIR /app
COPY target/somehome-1.jar .

# open ports
EXPOSE 8080

# run the application in production mode
CMD ["java", "-Dspring.profiles.active=production", "-Dserver.port=8080", "-jar", "somehome-1.jar"]