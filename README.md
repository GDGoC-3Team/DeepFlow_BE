# DeepFlow — 독서 세션 백엔드

## 프로젝트 소개

DeepFlow는 독서 앱의 서버 사이드를 담당하는 Spring Boot 기반 REST API 백엔드입니다. 사용자의 독서 세션을 기록하고 AI를 통해 집중도를 분석하며, 문장 피드·하이라이트·저장 문장 등 독서 경험 전반을 지원하는 기능을 제공합니다.

Firebase ID Token을 기반으로 인증을 처리하고, Google Cloud Storage(GCS)에 파일을 저장하는 구조로 설계되었습니다.

본 저장소는 GDGoC(Google Developer Groups on Campus) 3팀 프로젝트의 백엔드 브랜치입니다.

## 문제 정의

책을 읽고 싶다는 마음은 있지만, 실제 독서로 이어지지 못하는 경우가 많습니다.
가장 큰 원인 중 하나는 **시작의 어려움**입니다. deepflow는 이 문제에 집중합니다.

소설의 첫 문장은 작가가 가장 공들이는 문장으로, 작품의 분위기를 가장 압축적으로 담고 있습니다. 이 첫 문장을 매일 큐레이션하여 제공하고, 흥미를 느낀 작품의 도입부를 직접 읽을 수 있도록 연결함으로써, 부담 없이 독서를 시작할 수 있는 흐름을 만들고자 했습니다.


## 주요 기능

### 1. 첫 문장 피드
- **문장 피드** — 홈 화면용 문장 피드와 문장 상세 조회를 제공합니다.
  - 관련 파일: `SentenceController.java`, `SentenceService.java`, `HomeFeedResponse.java`

- **문장 저장** — 마음에 드는 문장을 컬렉션에 추가하고, 마이페이지에서 조회합니다.
  - 관련 파일: `SentenceController.java`, `SentenceService.java`

### 2. 오늘의 읽기

- **AI 집중도 분석** — 독서 세션 데이터를 기반으로 OpenAI API를 호출해 집중도 분석 결과를 반환합니다.
  - 관련 파일: `FocusAnalysisService.java`, `OpenAiFocusAnalysisClient.java`, `FocusAnalysisPromptBuilder.java`

- **하이라이트** — 독서 중 선택한 문장 범위를 하이라이트로 저장·삭제하고, 오프셋 범위를 검증합니다.
  - 관련 파일: `ReadingService.java`(`createHighlight`, `deleteHighlight`, `resolveOffsetRange`, `validateHighlightRange`), `Highlight.java`

### 3. 컬렉션, 기록 관리 및 설정

- **저장 문장(Saved Sentence)** — 하이라이트 또는 일반 문장을 저장하고, 정렬 기준(`SavedSentenceSort`)에 따라 조회합니다.
  - 관련 파일: `SavedSentenceController.java`, `SavedSentenceService.java`, `SavedSentenceType.java`

- **독서 캘린더 & 통계** — 날짜별 완료 현황, 월간 캘린더, 독서 습관 통계(`habit`)를 제공합니다.
  - 관련 파일: `ReadingService.java`(`calendar`, `completedDates`, `getReadingHabbit`, `secondsPerCharacter`)

- **사용자 설정** — 폰트 패밀리, 폰트 크기, 알림 시간·토큰·선호도를 개별적으로 조회/수정합니다.
  - 관련 파일: `UserSettingController.java`, `UserSettingService.java`, `FontFamily.java`, `FontFamilyConverter.java`

- **알림 스케줄러** — 설정된 알림 시간에 맞춰 FCM 알림을 발송합니다.
  - 관련 파일: `NotificationScheduler.java`
  
- **Firebase 인증** — 클라이언트가 전달한 Firebase ID Token을 서버에서 검증합니다.
  - 관련 파일: `FirebaseTokenFilter.java`, `FirebasePrincipal.java`, `FirebaseConfig.java`


## 기술 스택

| 분류 | 기술 |
|------|------|
| **언어** | Java 17 |
| **프레임워크** | Spring Boot 3.3.4 |
| **빌드 도구** | Gradle (Groovy DSL) |
| **데이터베이스** | MySQL 8, Spring Data JPA |
| **보안** | Spring Security, Firebase Admin SDK |
| **AI 연동** | OpenAI API (RestClient 기반) |
| **클라우드 스토리지** | Google Cloud Storage |
| **API 문서** | Springdoc OpenAPI (Swagger UI) |
| **컨테이너** | Docker, Docker Compose |
| **인증** | Firebase ID Token |

## 아키텍처 및 구조

![architectureMap.png](src/main/resources/architectureMap.png)

**다이어그램 근거:**
- `FirebaseTokenFilter`, `FirebasePrincipal`: `auth/` 패키지 파일 확인
- `Controller 계층`: `domain/` 하위 각 도메인의 `*Controller.java` 파일 확인
- `Service 계층`: `*Service.java` 및 `NotificationScheduler.java` 파일 확인
- `FocusAnalysisService` → OpenAI: `OpenAiFocusAnalysisClient.java`의 `restClient`, `apiKey`, `model` 필드 확인
- `Repository 계층`: 각 도메인의 `*Repository.java` 파일 확인
- MySQL: `application.yml`의 `jdbc:mysql://` 설정 확인
- Firebase FCM: `NotificationScheduler.java`와 `settings/` 도메인의 `NotificationTokenSettingRequest.java` 확인

---

이 프로젝트는 도메인 패키지 단위로 Controller → Service → Repository를 일관되게 구성했습니다. 각 도메인(`book`, `reading`, `saved`, `sentence`, `settings`, `user`)은 자신의 책임 범위 내에서 독립적으로 동작하며, 횡단 관심사(인증, 에러 처리, 응답 포맷)는 `auth/`, `common/`, `config/` 패키지에서 공통으로 처리합니다. 외부 서비스(OpenAI, Firebase, GCS)와의 연동은 각각 별도 클래스로 분리해 테스트와 교체가 용이하도록 했습니다.

## 핵심 구현 포인트

**1. Firebase ID Token 기반 인증 통합**
Spring Security 필터 체인에 `FirebaseTokenFilter`를 직접 삽입해 Firebase Admin SDK로 토큰을 검증하고, 검증된 사용자를 `FirebasePrincipal` 형태로 Security Context에 저장합니다. JWT를 서버에서 발급하지 않고 Firebase에서 발급한 토큰을 그대로 신뢰하는 구조입니다. 관련 파일: `FirebaseTokenFilter.java`, `FirebasePrincipal.java`, `SecurityConfig.java`, `FirebaseConfig.java`

**2. OpenAI 기반 독서 집중도 분석**
`OpenAiFocusAnalysisClient`는 Spring의 `RestClient`를 사용해 OpenAI Chat Completions API를 호출합니다. `FocusAnalysisPromptBuilder`가 프롬프트를 생성하고, 응답을 `FocusAnalysisResult`로 역직렬화하는 역할을 분리했습니다. `FocusAnalysisService`가 분석 흐름을 제어합니다. 관련 파일: `OpenAiFocusAnalysisClient.java`, `FocusAnalysisPromptBuilder.java`, `FocusAnalysisService.java`, `LlmAnalysisException.java`

**3. 페이지별 독서 시간 기록 및 읽기 속도 계산**
`ReadingService.recordPageTime()`은 페이지 단위로 체류 시간을 누적하고, `secondsPerCharacter()`로 초당 읽은 글자 수를 계산해 독서 결과에 반영합니다. `resolveOffsetRange()`와 `validateHighlightRange()`는 하이라이트 생성 시 유효한 텍스트 오프셋 범위를 검증합니다. 관련 파일: `ReadingService.java`, `PageTime.java`, `PageTimeRepository.java`

**4. 공통 응답 포맷 및 예외 처리**
모든 컨트롤러 응답은 `ApiResponse<T>` 제네릭 래퍼를 사용하며, `GlobalExceptionHandler`가 Validation 오류, 타입 불일치, Firebase 인증 오류, LLM 분석 오류, Not Found 등 10개 이상의 예외 유형을 HTTP 상태 코드에 맞춰 일관된 형식으로 처리합니다. 관련 파일: `ApiResponse.java`, `GlobalExceptionHandler.java`, `ErrorCode.java`

## 트러블슈팅 및 기술적 고민

- **Firebase 인증과 Spring Security 통합 방식**에 있어, 서버가 JWT를 직접 발급할 것인지에 대해 고민이 많았고. 사용자 정체성의 기준을 서버가 아니라 Firebase UID 하나로 고정하고자 하지 않고 Firebase ID Token만 신뢰하는 구조를 선택하였습니다.
- **OpenAI 응답 역직렬화 처리**에 있어, LLM 응답이 항상 예상한 JSON 구조를 반환하지 않을 때의 예외 처리에 있어 고민이 많았고, `LlmAnalysisException`를 별도로 만들어 관리하였습니다.
- **Docker Compose에서 Firebase 인증 정보를 안전하게 주입하는 방법**에 있어, firebase-credential.json을 어떻게 전달할지에 대해 고민이 많았고 `docker-compose.yml`이 파일을 시크릿으로 마운트하는 방식을 채택(`/run/secrets/firebase-credentials.json`)햐였습니다.

## 설치 및 실행 방법

### 사전 요구 사항

- JDK 17
- Docker, Docker Compose
- Firebase 서비스 계정 JSON 파일
- (선택) GCP 서비스 계정 키

### 로컬 실행 (MySQL만 Docker, 앱은 로컬 JVM)

```bash
# 1. MySQL만 컨테이너로 실행
docker compose up -d mysql

# 2. Firebase 인증 파일 배치
src/main/resources/firebase-credentials.json

# 3. 백엔드 실행 (Windows)
set JAVA_HOME=C:\Program Files\Java\jdk-17
.\gradlew.bat bootRun --no-daemon

# 3. 백엔드 실행 (macOS/Linux)
./gradlew bootRun --no-daemon
```

로컬 실행 시 MySQL 접속 URL: `jdbc:mysql://localhost:3306/deepflowdb`

### Docker Compose 전체 실행 (백엔드 + MySQL)

```bash
docker compose up --build
```

컨테이너 간 접속 URL: `jdbc:mysql://mysql:3306/deepflowdb`

### 환경 변수

| 변수 | 설명 |
|------|------|
|------|------|
| `SPRING_PROFILES_ACTIVE` | 스토리지 프로필 (`gcp` 또는 `aws`) |
| `SPRING_DATASOURCE_URL` | MySQL JDBC URL |
| `DB_USERNAME` | DB 사용자명 |
| `DB_PASSWORD` | DB 비밀번호 |
| `FIREBASE_CREDENTIALS_PATH` | Firebase 서비스 계정 JSON 파일 경로 |
| `GCP_PROJECT_ID` | GCP 프로젝트 ID (gcp 프로필 사용 시) |
| `GCS_BUCKET` | GCS 버킷 이름 (gcp 프로필 사용 시) |
| `GCP_SERVICE_ACCOUNT_KEY` | GCP 서비스 계정 키 JSON |


## 폴더 구조

```
DeepFlow_BE/
├── .github/
│   └── ISSUE_TEMPLATE/           # issue 템플릿
│   └── PULL_REQUEST_TEMPLATE/    # PR 템플릿
├── src/main/java/com/deepflow/app/
│   ├── AppApplication.java       # 스프링 부트 엔트리포인트
│   ├── auth/                     # Firebase 인증 (토큰 필터, Principal)
│   ├── common/                   # 공통 응답(ApiResponse), 예외 처리, BaseEntity
│   ├── config/                   # Firebase, GCS, Security, Swagger 설정
│   ├── domain/
│   │   ├── book/                 # 책 및 페이지 엔티티, 레포지토리, 서비스
│   │   ├── reading/              # 독서 세션, 하이라이트, AI 집중도 분석
│   │   ├── saved/                # 저장 문장 (생성, 조회, 정렬, 타입)
│   │   ├── sentence/             # 문장 피드 (홈 피드, 상세)
│   │   ├── settings/             # 사용자 설정 (폰트, 알림)
│   │   └── user/                 # 사용자 엔티티, 서비스
│   ├── notification/             # FCM 알림 스케줄러
│   └── storage/                  # 클라우드 스토리지 인터페이스 및 GCS 구현체
├── src/main/resources/
│   ├── application.yml           # 기본 설정
│   ├── application-gcp.yml       # GCP 스토리지 프로필 설정
│   ├── data-seed.sql             # 초기 데이터 시드
│   └── init.sql                  # DB 초기화 스크립트
├── Dockerfile                    # 백엔드 이미지 빌드
├── Dockerfile.mysql              # MySQL 커스텀 이미지
├── docker-compose.yml            # 로컬 개발 환경 (백엔드 + MySQL)
├── build.gradle                  # Gradle 의존성 및 빌드 설정
├── AGENTS.md                     # AI 코딩 에이전트용 구조 가이드
└── db-schema.html                # DB 스키마 시각화
```

## 배운 점

- **Firebase와 Spring Security의 통합 방법**: JWT 직접 발급 없이 Firebase ID Token을 서버에서 검증하고 Spring Security의 인증 컨텍스트에 반영하는 필터 기반 통합 패턴을 익혔습니다.
- **OpenAI API를 Spring RestClient로 연동하는 방법**: `RestClient`를 이용한 외부 LLM API 호출, 프롬프트 빌더 분리, 응답 역직렬화 패턴을 학습했습니다.
- **Spring Profile을 활용한 전략 패턴**: 인터페이스와 `@Profile` 어노테이션 조합으로 클라우드 스토리지 구현체를 환경 변수 하나만으로 교체하는 설계를 경험했습니다.
- **도메인 단위 패키지 구조**: 기능별이 아닌 도메인 단위로 패키지를 나누어 각 도메인의 응집도를 높이는 레이어드 아키텍처를 적용했습니다.
- **Docker Compose를 활용한 개발 환경 구성**: 백엔드와 MySQL을 컨테이너로 함께 운영하는 환경을 다뤘습니다.

## 향후 개선 사항

- **테스트 코드 보강**: 현재 저장소에서 `test/` 디렉토리 구조가 확인되지 않아 단위 테스트 및 통합 테스트 추가가 필요합니다.
- **OpenAI 응답 안정성 향상**: LLM 응답 형식이 달라질 경우를 대비한 폴백 처리 및 재시도 로직 추가
- **알림 스케줄러 상세화**: `NotificationScheduler`에서 발송 실패 시 재시도 정책, 발송 이력 기록 기능 추가
- **API 버저닝**: 클라이언트 하위 호환성을 위한 API 버전 관리 도입
- **Swagger 문서 보강**: 각 엔드포인트에 대한 요청/응답 예시 및 에러 케이스 문서화
- **환경 변수 검증**: 애플리케이션 시작 시 필수 환경 변수 누락을 즉시 감지하는 설정 검증 로직 추가
