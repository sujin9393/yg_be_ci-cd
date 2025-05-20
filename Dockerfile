# 1단계: jar 빌드
FROM gradle:8.4.0-jdk21 AS builder
WORKDIR /app #현재 프로젝트 소스 /app에 복사 
COPY . .
RUN gradle bootJar -x test

# 2단계: 실행 이미지
# 위에서 만든 jar 파일만 복사해서 사용 
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
