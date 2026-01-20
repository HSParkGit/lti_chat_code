# Lineus Backend – API Endpoint Guide

This is a concise map of the primary REST endpoints and their intent. For detailed request/response schemas, see the DTOs under `src/main/java/.../api/dto` and the asciidoc specs in `src/docs/asciidoc/api/`.

## Auth
- `POST /api/v1/auth/refresh`  
  Regenerate an access token from a valid refresh token.  
  Request:
  ```json
  { "refreshToken": "xxx" }
  ```
  Response:
  ```json
  { "accessToken": "...", "refreshToken": "...", "expiresIn": 3600 }
  ```

## SSO
- `POST /api/v1/sso/authenticate`  
  LTI/Canvas SSO entrypoint.  
  Request:
  ```json
  { "canvasUserId": 5, "email": "joe@lomtech.net", "name": "Joe", "loginId": "joe" }
  ```
  Response:
  ```json
  { "userId": 3, "lmsUserId": 5, "accessToken": "...", "refreshToken": "...", "role": "STUDENT" }
  ```
- `GET /api/v1/sso`  
  Health/test. Response: `Hello, World!`

## Chat System (`/api/v1/chat`)
- `GET /users/search?course_id&search_term[&as_user_id]`  
  Search Canvas course users (excludes the caller).
  Response (array):
  ```json
  [{ "id": 5, "name": "Joe", "loginId": "joe", "email": "joe@lomtech.net", "avatarUrl": "...", "shortName": "Joe" }]
  ```
- `POST /direct`  
  Create or reopen a direct chat between the caller and `otherUserId`. Reuses existing chat; if caller had deleted it, old messages are hidden to the caller, preserved for the peer.
  Request:
  ```json
  { "otherUserId": 5 }
  ```
  Response (Chat):
  ```json
  { "chatId": "...", "type": "direct", "participants": [{ "id": 3 }, { "id": 5 }], "lastMessage": null, "unreadCount": 0 }
  ```
- `POST /messages`  
  Send a message to a chat (supports reply-to and messageType).
  Request:
  ```json
  { "chatId": "...", "content": "Hi", "messageType": "text", "replyToMessageId": null }
  ```
  Response (Message):
  ```json
  { "messageId": "...", "chatId": "...", "senderId": 3, "content": "Hi", "timestamp": "2025-12-30T10:00:00", "messageType": "text" }
  ```
- `GET /conversations`  
  List all chats visible to the caller (filters out deleted membership).
  Response (array of Chat):
  ```json
  [{ "chatId": "...", "type": "direct", "lastMessage": {...}, "unreadCount": 1, "participants": [{ "id": 3 }, { "id": 5 }] }]
  ```
- `GET /conversations/unread`  
  List chats that have unread messages for the caller.
- `GET /conversations/{chatId}/messages?page=0&size=50`  
  Paginated messages. If the caller previously deleted the chat, only messages after their `deletedAt` are returned.
  Response (Page):
  ```json
  { "content": [ { "messageId": "...", "content": "Hi" } ], "pageable": {...}, "totalPages": 1, "totalElements": 1 }
  ```
- `GET /unread`  
  Unread counts per chat for the caller.
  Response:
  ```json
  [{ "chatId": "...", "unreadCount": 2 }]
  ```
- `POST /conversations/{chatId}/read`  
  Mark latest message as read for the caller.
- `DELETE /conversations/{chatId}`  
  Soft-delete chat for the caller; hides history for that user only.
- `POST /conversations/{chatId}/mute` / `POST /conversations/{chatId}/unmute`  
  Toggle mute for the caller.

## Notes
- All endpoints require JWT auth unless explicitly a health/test endpoint.
- Chat soft-delete is per participant: one user’s delete does not remove history for others. When a user reopens, they see only messages after their own `deletedAt`.
- Caching: message and unread counts use `@Cacheable` (`chat:messages`, `chat:unreadCounts`) and are evicted on mutations.


