# Lineus LTI 프로젝트 개요

이 디렉토리에는 **Canvas LMS와 연동되는 LTI(Learning Tools Interoperability) 기반 LMS 시스템**이 포함되어 있습니다.

---

## 프로젝트 구성

| 폴더/파일 | 설명 |
|----------|------|
| `Lineus-Backend-feat-lineusPhase3/` | Spring Boot 백엔드 (LMS 핵심 API) |
| `Lineus-Frontend-feat-lineusPhase3/` | React 프론트엔드 (채팅/그룹 관리 UI) |
| `lineus-lti-service-master/` | LTI 1.3 인증 서비스 (Canvas 연동) |
| `LTI_개발_가이드.pdf` | 설치 및 Canvas 설정 가이드 |

---

## 1. 백엔드 (Lineus-Backend)

### 개요
- **기술 스택**: Spring Boot 3.1.2, Java 17, MySQL, Redis, JPA/QueryDSL
- **주요 기능**: 인증(Auth), 채팅 시스템, SSO, 미디어 관리, Canvas API 연동
- **포트**: 8080

### 필수 요구사항
- JDK 17+
- Docker (MySQL, Redis 실행용) 또는 로컬 설치
- Gradle Wrapper (`./gradlew`)

### 실행 방법

```bash
# 1. 디렉토리 이동
cd Lineus-Backend-feat-lineusPhase3

# 2. 인프라 실행 (Docker)
docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=db_lineus mysql:8
docker run -d -p 6379:6379 redis:7

# 3. 환경 변수 설정
cp .env .env.local
# .env.local 파일 편집하여 DB/Redis 접속 정보 설정

# 4. 애플리케이션 실행
./gradlew bootRun

# 5. 빌드 (JAR 생성)
./gradlew clean build
# 결과물: build/libs/*.jar
```

### 주요 환경 변수 (.env)
| 변수명 | 설명 |
|--------|------|
| `db-url` | MySQL 접속 URL |
| `db-user` | DB 사용자명 |
| `db-pwd` | DB 비밀번호 |
| `redis-host` | Redis 호스트 |
| `redis-port` | Redis 포트 |
| `jwt-secret` | JWT 시크릿 키 |
| `canvas-url` | Canvas LMS URL |
| `token` | Canvas API 토큰 |

---

## 2. 프론트엔드 (Lineus-Frontend)

### 개요
- **기술 스택**: React 18, Vite, Tailwind CSS, Zustand, React Query
- **주요 기능**: 로그인/LTI 인증, 실시간 채팅(WebSocket), 그룹/프로젝트 관리, 다국어 지원(KR/EN/CH)
- **포트**: 5173 (개발), 8081 (Docker)

### 필수 요구사항
- Node.js (npm 또는 yarn)

### 실행 방법

```bash
# 1. 디렉토리 이동
cd Lineus-Frontend-feat-lineusPhase3

# 2. 의존성 설치
npm install
# 또는
yarn install

# 3. 환경 변수 설정
cp .env.sample .env.development
# .env.development 편집하여 API 경로 등 설정

# 4. 개발 서버 실행
npm run dev

# 5. 프로덕션 빌드
npm run build
# 결과물: dist/

# 6. Docker 배포
docker-compose up --build
```

### 주요 스크립트
| 명령어 | 설명 |
|--------|------|
| `npm run dev` | 개발 서버 실행 |
| `npm run build` | 프로덕션 빌드 |
| `npm run preview` | 빌드 결과 미리보기 |
| `npm run storybook` | UI 컴포넌트 문서 |
| `npm run lint` | ESLint 검사 |

---

## 3. LTI 서비스 (lineus-lti-service)

### 개요
- **기술 스택**: Spring Boot 3.5.5, Java 17
- **주요 기능**: LTI 1.3 인증/인가, Canvas LMS 연동
- **포트**: 9090

### 실행 방법

```bash
# 1. 디렉토리 이동
cd lineus-lti-service-master

# 2. 환경 변수 설정 (없으면 생성)
# .env 파일에 아래 내용 설정

# 3. 빌드
./gradlew build

# 4. 실행
./gradlew bootRun
```

### 주요 환경 변수
| 변수명 | 설명 |
|--------|------|
| `SERVER_PORT` | 서버 포트 (기본: 9090) |
| `CANVAS_BASE_URL` | Canvas LMS URL |
| `CANVAS_CLIENT_ID_CHAT_ROOM` | Canvas Client ID |
| `LTI_BASE_URL` | LTI 서비스 URL (리다이렉트용) |
| `FRONTEND_BASE_URL` | 프론트엔드 URL |

---

## 전체 시스템 아키텍처

```
┌─────────────────┐      ┌─────────────────┐      ┌─────────────────┐
│   Canvas LMS    │─────▶│  LTI Service    │─────▶│    Frontend     │
│                 │ LTI  │   (Port 9090)   │      │   (Port 5173)   │
└─────────────────┘      └─────────────────┘      └────────┬────────┘
                                                           │
                                                           ▼
                         ┌─────────────────┐      ┌─────────────────┐
                         │     MySQL       │◀─────│    Backend      │
                         │   (Port 3306)   │      │   (Port 8080)   │
                         └─────────────────┘      └────────┬────────┘
                                                           │
                         ┌─────────────────┐               │
                         │     Redis       │◀──────────────┘
                         │   (Port 6379)   │
                         └─────────────────┘
```

---

## 로컬 개발 전체 실행 순서

```bash
# 1. 인프라 실행
docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=db_lineus mysql:8
docker run -d -p 6379:6379 redis:7

# 2. 백엔드 실행 (새 터미널)
cd Lineus-Backend-feat-lineusPhase3
./gradlew bootRun

# 3. LTI 서비스 실행 (새 터미널)
cd lineus-lti-service-master
./gradlew bootRun

# 4. 프론트엔드 실행 (새 터미널)
cd Lineus-Frontend-feat-lineusPhase3
npm install && npm run dev
```

---

## Canvas LMS 연동 설정

Canvas에서 LTI 도구를 설정하려면 `LTI_개발_가이드.pdf` 문서를 참고하세요.

**요약:**
1. Canvas Admin > Developer Keys > + Developer Key > LTI Key
2. Redirect URI: `https://[LTI_서비스_URL]/lti/authorized-redirect`
3. Target Link URI: `https://[LTI_서비스_URL]/lti/launch`
4. OpenID Connect Initiation URL: `https://[LTI_서비스_URL]/lti/login-initiation`
5. Client ID 저장 후 Account Settings > Apps에서 앱 등록

---

## 참고 문서

- `Lineus-Backend-feat-lineusPhase3/README.md` - 백엔드 상세 가이드
- `Lineus-Backend-feat-lineusPhase3/API_ENDPOINTS.md` - API 엔드포인트 목록
- `Lineus-Frontend-feat-lineusPhase3/README.md` - 프론트엔드 상세 가이드
- `lineus-lti-service-master/README.md` - LTI 서비스 가이드
- `lineus-lti-service-master/ENDPOINTS.md` - LTI 엔드포인트 목록
- `lineus-lti-service-master/CANVAS_SETUP.md` - Canvas 설정 상세
