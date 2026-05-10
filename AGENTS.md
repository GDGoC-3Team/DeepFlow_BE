# AGENTS.md

Instructions for agents working on this project.

## Project Overview

This is a Spring Boot backend project named `deepflow`.

- Java: 17
- Spring Boot: 3.3.4
- Build tool: Gradle, Groovy DSL
- Root package: `com.deepflow.app`
- Source root: `src/main/java/com/deepflow/app`
- Main class: `com.deepflow.app.AppApplication`
- Database: MySQL, managed by JPA `ddl-auto=update`
- Authentication: Firebase ID token verification only
- Storage profiles: `aws` for S3, `gcp` for Google Cloud Storage

## Required Structure

Keep the current package layout:

```text
src/main/java/com/deepflow/app/
  AppApplication.java
  auth/
  common/
  config/
  domain/
    book/
    reading/
    saved/
    sentence/
    settings/
    user/
  notification/
  storage/
```

## Configuration Files

Maintain these files:

```text
build.gradle
settings.gradle
gradlew
gradlew.bat
docker-compose.yml
.gitignore
.github/workflows/ci.yml
src/main/resources/application.yml
src/main/resources/application-aws.yml
src/main/resources/application-gcp.yml
```

Firebase credentials are loaded from the classpath:

```text
src/main/resources/firebase-credentials.json
```

Do not change Firebase auth to issue JWTs. The server verifies Firebase ID tokens only.

## Build And Verification

On this Windows workspace, use JDK 17 explicitly:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-17'
.\gradlew.bat clean build --no-daemon
```

For a faster compile check:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-17'
.\gradlew.bat compileJava --no-daemon
```

CI uses:

```sh
sh ./gradlew build --no-daemon
sh ./gradlew test --no-daemon
```

## Implementation Rules

- Keep package names under `com.deepflow.app`.
- Do not reintroduce `com.example.app` or `src/main/java/com/example/app`.
- Use Spring components, repositories, services, and controllers following the existing domain package pattern.
- Use Lombok where the current code already uses it.
- Use `ApiResponse<T>` for controller responses.
- Use `GlobalExceptionHandler` for common API errors.
- Keep cloud storage implementations profile-specific:
  - `S3StorageService` with `@Profile("aws")`
  - `GcsStorageService` with `@Profile("gcp")`
- Keep Firebase credentials classpath-based in `FirebaseConfig`.
- Keep scheduled notifications in `NotificationScheduler`.

## Explicit Non-Goals

Do not add these unless the user explicitly asks:

- Kakao login
- Server-issued JWT auth
- Redis
- Flyway
- Full cloud deployment infrastructure

## Local Development

Start MySQL with:

```powershell
docker compose up -d
```

Create a local `.env` for secrets if needed. `.env` must stay ignored by Git.

Set storage profile with:

```text
SPRING_PROFILES_ACTIVE=aws
```

or:

```text
SPRING_PROFILES_ACTIVE=gcp
```
