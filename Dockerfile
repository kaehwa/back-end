# 1. JDK 21 기반 이미지 사용
FROM eclipse-temurin:21-jdk-alpine AS builder

  # 2. 빌드 결과물 복사 (Gradle 빌드 시 build/libs/*.jar)
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

  # 3. 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
