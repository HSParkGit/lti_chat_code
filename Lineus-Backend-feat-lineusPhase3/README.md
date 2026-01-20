# Lineus Backend – Project Flow & Developer Guide

## Overview
- Spring Boot service for LMS features (auth, chat, SSO, media, etc.).
- Modules of note: `feature/chatSystem`, `feature/auth`, `feature/sso`, global error handling in `global/error`.
- Persistence: MySQL (kumoh datasource), Redis for caching, Flyway migrations in `src/main/resources/db/migration`.

## Architecture & Flow (high level)
- Entry: `kr.lineedu.lms.BaseApplication` with `@ConfigurationPropertiesScan` (reads `.env.*` via dotenv).
- Security/JWT: filters in `config/jwt`, roles under `global/enums`.
- Chat:
  - Entities: `Chat`, `Participant`, `Message`.
  - Soft-delete per participant (`Participant.deleted`, `deletedAt`) hides history for that user; direct chat reuse keeps the other user’s history intact.
  - Services in `feature/chatSystem/api/ChatService`, REST controllers in `feature/chatSystem/api/controller`.
- Error handling: `global/error/GlobalExceptionHandler` returns structured `ErrorResponse` (watch for SSE endpoints—prefer to avoid JSON on `text/event-stream`).

## Prerequisites
- JDK 17+
- Docker (recommended for MySQL/Redis) or local MySQL/Redis
- Gradle wrapper (`./gradlew`)

## Configuration
- Base config: `src/main/resources/config/application.yml` (profile switcher).
- Profiles:
  - `application-local.yml` (default when `Spring.profile.active` not set).
  - `application-prod.yml`, `application-test.yml`.
- Env variables (via `.env` or deployment env):
  - DB: `db-url`, `db-user`, `db-pwd`
  - Redis: `redis-host`, `redis-port`, `redis-pwd` (if any)
  - JWT: `jwt-secret`, `jwt-access-token-time`
  - Canvas: `token`, `canvas-url`
  - URLs: `server-url`
  - Files (if needed): `file-dir`, `file-base-url`

## Local Run
1) Start infra (example):
   - MySQL: `docker run -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=db_lineus mysql:8`
   - Redis: `docker run -p 6379:6379 redis:7`
2) Create `.env.local` (or export vars) matching your DB/Redis.
3) Run: `./gradlew bootRun`
4) Default port: `8080`

## Build
- Jar: `./gradlew clean build` (artifacts under `build/libs`)
- Tests only: `./gradlew test`

## Testing Checklist
- Unit/integration: `./gradlew test`
- Chat regressions:
  - A deletes a direct chat; B keeps history.
  - A reopens chat with B: A sees only messages after delete; B sees full thread; no new room created.
- Verify SSE endpoints handle errors without forcing JSON on `text/event-stream`.

## Notable Conventions
- Soft-delete flags used instead of hard deletes (participants, messages visibility).
- DTOs in `feature/*/api/dto`; controllers keep thin logic; services own business rules.
- Caching: `chat:messages`, `chat:unreadCounts` use `@Cacheable/@CacheEvict`.

## Adding Migrations
- Place new SQL in `src/main/resources/db/migration` with `Vx__desc.sql`.
- Keep `spring.flyway.enabled` aligned with the target profile (disabled in prod YAML currently; enable as needed).

## Troubleshooting
- DB connection refused: confirm `db-url` host/port reachable from app container/host.
- Missing multipart props: set `spring.servlet.multipart.max-file-size` and `max-request-size` in active profile.
- SSE error responses: adjust exception handling for `text/event-stream` to return simple text/event format if needed.


