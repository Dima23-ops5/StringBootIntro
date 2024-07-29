# Builder stage
FROM openjdk:17-jdk-alpine AS builder
WORKDIR /spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} spring.jar
RUN java -Djarmode=layertools -jar spring.jar extract

# Final stage
FROM openjdk:17-jdk-alpine
WORKDIR /spring
COPY --from=builder /spring/dependencies/ ./
COPY --from=builder /spring/spring-boot-loader/ ./
COPY --from=builder /spring/snapshot-dependencies/ ./
COPY --from=builder /spring/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
EXPOSE 8080
