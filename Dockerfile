# 1. OpenJDK 17 이미지를 기반으로 사용
FROM openjdk:21-jdk-slim

LABEL authors="sangjun"

# 2. 작업 디렉토리 생성
WORKDIR /app

# 3. 빌드된 JAR 파일을 컨테이너 내부로 복사
COPY build/libs/*.jar app.jar

# 4. 애플리케이션 실행
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]