FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . .
RUN chmod +x ./mvnw
RUN ./mvnw clean install -DskipTests
CMD ["java", "-jar", "target/gestion_stock.jar"]
