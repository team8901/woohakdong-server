# Woohakdong Server 실행 가이드

## 환경별 실행 방법

### 1. Local 환경 (Docker 사용 X)
아래의 3가지 방법 중 하나로 실행합니다.
```bash
# 1. IntelliJ에서 실행
Configuration - Edit 확인
Build and Run 탭에서 Active Profiles에서 local 입력하여 실행

# 2. application-local.yaml 프로필로 직접 실행
./gradlew bootRun --args='--spring.profiles.active=local'

# 3. 빌드 후 실행
./gradlew bootJar
java -jar -Dspring.profiles.active=local build/libs/*.jar
```

### 2. Development 환경 (Docker Compose 사용)

```bash
# .env.dev 파일의 환경변수를 사용하여 실행
docker-compose --env-file .env.dev up -d

# 빌드와 함께 실행
docker-compose --env-file .env.dev up -d --build

# 로그 확인
docker-compose --env-file .env.dev logs -f
```

### 3. Production 환경 (Docker Compose 사용)

```bash
# .env.prod 파일의 환경변수를 사용하여 실행
docker-compose --env-file .env.prod up -d

# 빌드와 함께 실행
docker-compose --env-file .env.prod up -d --build

# 로그 확인
docker-compose --env-file .env.prod logs -f
```

### 4. Docker Compose MySQL 포함 실행

Docker Compose에 MySQL 컨테이너가 포함되어 있습니다.

```bash
# 전체 서비스 실행 (MySQL + Application)
docker-compose up -d

# MySQL만 실행
docker-compose up -d mysql

# 로그 확인
docker-compose logs -f mysql

# MySQL 컨테이너 접속
docker exec -it woohakdong-mysql mysql -uwoohakdong -pwoohakdong123
```

## 주의사항

- Local 환경에서는 MySQL이 로컬(localhost:3306)에 실행 중이어야 합니다.
- Local 환경의 경우, Firebase 설정 파일(`firebase-service-account.json`)이 `src/main/resources/` 디렉토리에 있어야 합니다.
- Dev/Prod 환경에서는 `.env.dev` 또는 `.env.prod` 파일의 데이터베이스 및 외부 서비스 설정을 환경에 맞게 수정해야 합니다.
- Dev/Prod 환경에서는 Firebase 설정 파일을 마운트를 통해서 주입합니다.

## 환경 변수 설정

각 환경의 `.env` 파일에서 다음 항목들을 설정해야 합니다:

- `SPRING_DATASOURCE_URL`: 데이터베이스 연결 URL
- `SPRING_DATASOURCE_USERNAME`: 데이터베이스 사용자명
- `SPRING_DATASOURCE_PASSWORD`: 데이터베이스 비밀번호
- `JWT_SECRET`: JWT 토큰 서명용 비밀키
- `AWS_S3_BUCKET_NAME`: S3 버킷 이름
- `AWS_ACCESS_KEY_ID`: AWS 액세스 키
- `AWS_SECRET_ACCESS_KEY`: AWS 시크릿 키

## MySQL 연결 설정

### Docker Compose 내부 통신 (권장)
애플리케이션이 Docker Compose 네트워크 내에서 MySQL에 연결할 때:
```
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/woohakdong?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul
```

### 로컬 개발 환경에서 접속
로컬에서 Docker MySQL에 연결할 때:
```
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/woohakdong?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul
```

### MySQL 기본 설정값
- **데이터베이스**: woohakdong
- **사용자**: woohakdong
- **비밀번호**: woohakdong123
- **포트**: 3306

### .env 파일 예시
```env
# MySQL 설정
MYSQL_ROOT_PASSWORD=rootpassword
MYSQL_DATABASE=woohakdong
MYSQL_USER=woohakdong
MYSQL_PASSWORD=woohakdong123

# Spring 데이터소스 설정
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/woohakdong?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul
SPRING_DATASOURCE_USERNAME=woohakdong
SPRING_DATASOURCE_PASSWORD=woohakdong123
```