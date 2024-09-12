FROM eclipse-temurin:17-alpine
WORKDIR /app

 COPY build/libs/*SNAPSHOT.jar app.jar

EXPOSE 8080
CMD java -jar app.jar