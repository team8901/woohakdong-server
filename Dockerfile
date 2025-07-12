# 1단계: 빌드 단계 (Gradle + JDK 포함 이미지)
FROM gradle:8.4-jdk21 AS build
WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN gradle bootJar --no-daemon

# 2단계: 실행 단계 (작은 JDK 이미지)
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]