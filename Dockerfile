# 1단계: Gradle로 JAR 빌드
FROM gradle:8.4.0-jdk21 AS builder

# 작업 디렉토리 설정
WORKDIR /app

# 프로젝트 전체 복사
COPY . .

# JAR 빌드 후, jar 파일을 app.jar로 복사
RUN gradle bootJar -x test && cp build/libs/*.jar app.jar


# 2단계: JAR 실행용 경량 이미지
FROM openjdk:21-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 앞 단계에서 만든 app.jar 복사
COPY --from=builder /app/app.jar app.jar

# 8080 포트 오픈
EXPOSE 8080

# Spring Boot 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]