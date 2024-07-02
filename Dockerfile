FROM gradle:7.6-jdk17 AS build

# 작업 디렉토리 설정
WORKDIR /home/gradle/project

# 모든 소스 파일 복사
COPY . .

# 애플리케이션 빌드
RUN gradle build --no-daemon

# 2단계: 실행 단계
FROM openjdk:17-jdk-alpine

# 빌드 단계에서 생성된 JAR 파일을 복사
COPY --from=build /home/gradle/project/build/libs/app.jar /app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]