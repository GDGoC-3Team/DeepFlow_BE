# Deepflow

Deepflow is a Spring Boot backend for reading sessions, sentence feeds, saved sentences, user settings, notifications, Firebase authentication, and cloud file storage.

## Stack

- Java 17
- Spring Boot 3.3.4
- Gradle, Groovy DSL
- MySQL 8.0
- Spring Data JPA
- Spring Validation
- Spring Security
- Firebase Admin SDK 9.2.0
- Springdoc OpenAPI 2.6.0
- AWS SDK for S3
- Google Cloud Storage SDK
- Lombok
- JUnit / Spring Boot Test
- Docker Compose
- GitHub Actions CI

## Package

```text
com.deepflow.app
```

Source root:

```text
src/main/java/com/deepflow/app
```

## Main Features

- Firebase ID token verification
- User profile APIs
- Random sentence feed
- Save and unsave sentence logic
- Saved sentence listing with sorting
- Daily book selection
- Reading session tracking
- Page time tracking and reread detection
- Reading concentration result calculation
- User reading settings
- Scheduled FCM notifications
- AWS S3 storage profile
- GCP Cloud Storage profile
- Swagger UI and OpenAPI docs

### Firebase

Firebase credentials are loaded from:

```text
src/main/resources/firebase-credentials.json
```

The backend expects the client app to complete Google sign-in with Firebase and send the Firebase ID token to:

```http
POST /auth/login
Authorization: Bearer <firebase-id-token>
Content-Type: application/json

{
  "nickname": "optional",
  "fcmToken": "optional"
}
```

After login, call protected APIs with the same header format:

```http
Authorization: Bearer <firebase-id-token>
```

Error responses include a stable `code` field for client branching:

```json
{
  "success": false,
  "data": null,
  "code": "AUTH_TOKEN_INVALID",
  "message": "Invalid Firebase ID token"
}

## GCS Profile

Enable Google Cloud Storage with:

```powershell
$env:SPRING_PROFILES_ACTIVE='gcp'
```

Required environment variables:

```powershell
$env:GCP_PROJECT_ID='your-gcp-project-id'
$env:GCS_BUCKET='your-gcs-bucket'
$env:GCP_SERVICE_ACCOUNT_KEY='service-account-json'
```

`GCP_SERVICE_ACCOUNT_KEY` must contain the raw service account JSON used by the backend.
```
