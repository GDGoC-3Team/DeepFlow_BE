# Deepflow

Deepflow is a Spring Boot backend for reading sessions, sentence feeds, saved sentences, user settings, notifications, Firebase authentication, and cloud file storage.

## Stack

- Java 17
- Spring Boot 3.3.4
- Gradle, Groovy DSL
- MySQL 8
- Spring Data JPA
- Spring Security
- Firebase Admin SDK
- Springdoc OpenAPI
- Google Cloud Storage
- Docker Compose
- GitHub Actions

## Local Run

Backend can run locally while MySQL runs in Docker.

Start MySQL only:

```powershell
docker compose up -d mysql
```

Run the backend locally:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-17'
.\gradlew.bat bootRun --no-daemon
```

The local backend connects to MySQL through:

```text
jdbc:mysql://localhost:3306/deepflowdb
```

## Docker Run

Both backend and MySQL can also run in Docker Compose.

```powershell
docker compose up --build
```

Inside Compose, backend connects to MySQL through:

```text
jdbc:mysql://mysql:3306/deepflowdb
```

## Firebase Credentials

Firebase credentials are loaded in this order:

1. `firebase.credentials-path`
2. `src/main/resources/firebase-credentials.json`

For Docker and GitHub Actions, prefer runtime file injection instead of baking credentials into the image.

Local Docker Compose mounts:

```text
./src/main/resources/firebase-credentials.json
-> /run/secrets/firebase-credentials.json
```

and sets:

```text
FIREBASE_CREDENTIALS_PATH=/run/secrets/firebase-credentials.json
```

## GCP Profile

Enable the GCP storage profile with:

```powershell
$env:SPRING_PROFILES_ACTIVE='gcp'
```

Required variables:

```powershell
$env:GCP_PROJECT_ID='your-gcp-project-id'
$env:GCS_BUCKET='your-gcs-bucket'
$env:GCP_SERVICE_ACCOUNT_KEY='raw-service-account-json'
```

## GitHub Actions CI/CD

The workflow at `.github/workflows/ci.yml` does three things:

1. Starts a MySQL service container
2. Runs Gradle tests and builds `bootJar`
3. Builds the backend Docker image and pushes it to GHCR on `push`

On pull requests, the image is built but not pushed.

### Recommended GitHub Variables

Use `Repository settings -> Secrets and variables -> Actions`.

`vars`

- `SPRING_PROFILES_ACTIVE`
- `SPRING_DATASOURCE_URL`
- `GCP_PROJECT_ID`
- `GCS_BUCKET`

`secrets`

- `DB_USERNAME`
- `DB_PASSWORD`
- `FIREBASE_CREDENTIALS_JSON`
- `GCP_SERVICE_ACCOUNT_KEY`

### How Injection Works

GitHub Actions values are not injected automatically. The workflow must map them into:

- step environment variables with `env:`
- files created during the workflow
- `docker run` or deployment platform environment settings

Example:

```yaml
env:
  SPRING_DATASOURCE_URL: ${{ vars.SPRING_DATASOURCE_URL }}
  SPRING_DATASOURCE_USERNAME: ${{ secrets.DB_USERNAME }}
  SPRING_DATASOURCE_PASSWORD: ${{ secrets.DB_PASSWORD }}
```

Firebase JSON should be written to a file during the workflow:

```yaml
- name: Write Firebase credentials file
  run: |
    cat <<'EOF' > "$GITHUB_WORKSPACE/firebase-credentials.json"
    ${{ secrets.FIREBASE_CREDENTIALS_JSON }}
    EOF
    echo "FIREBASE_CREDENTIALS_PATH=$GITHUB_WORKSPACE/firebase-credentials.json" >> "$GITHUB_ENV"
```

That makes the file path available to Spring during the same job.
