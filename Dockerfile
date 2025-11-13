# 1단계: 빌드 단계 (Gradle + JDK 포함 이미지)
FROM gradle:8.4-jdk21 AS build
WORKDIR /app

# Gradle 의존성 먼저 캐싱 (소스 코드 변경 시 재다운로드 방지)
COPY --chown=gradle:gradle build.gradle settings.gradle ./
COPY --chown=gradle:gradle gradle ./gradle
RUN gradle dependencies --no-daemon || true

# 소스 코드 복사 및 빌드
COPY --chown=gradle:gradle . /app
RUN gradle bootJar --no-daemon

# 2단계: 실행 단계 (작은 JRE 이미지)
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]