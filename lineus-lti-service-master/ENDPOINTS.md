# API Endpoints Documentation

This document lists all available endpoints in the Lineus LTI application.

**Base URL:** `https://domain

---

## LTI Tool Configuration

### GET `/.well-known/lti-tool-configuration.json`
Returns the LTI tool configuration JSON. This endpoint is used by LMS systems (like Canvas) to auto-configure LTI tools using the "Enter URL" method.

**Response:**
- **Type:** `application/json`
- **Status:** `200 OK`
- **Body:** JSON object containing LTI tool configuration

**Configuration includes:**
- Tool title and description
- Target link URI (launch URL)
- OIDC initiation URL
- OAuth scopes
- Canvas-specific extensions
- Custom parameters

**Example:**
```bash
curl https://domain/.well-known/lti-tool-configuration.json
```

**Response Example:**
```json
{
  "title": "Lineus LTI Tool",
  "description": "Lineus Learning Tools Interoperability (LTI) Tool",
  "target_link_uri": "https://domain/lti/launch",
  "oidc_initiation_url": "https://domain/lti/login-initiation",
  "scopes": [
    "https://purl.imsglobal.org/spec/lti-ags/scope/lineitem",
    "https://purl.imsglobal.org/spec/lti-ags/scope/result",
    "https://purl.imsglobal.org/spec/lti-ags/scope/score",
    "https://purl.imsglobal.org/spec/lti-nrps/scope/contextmembership.readonly"
  ],
  "extensions": {
    "https://canvas.instructure.com/lti/tool_configuration": {
      "platform": "canvas.instructure.com",
      "settings": {
        "placements": [
          {
            "placement": "course_navigation",
            "message_type": "LtiResourceLinkRequest",
            "target_link_uri": "https://domain/lti/launch"
          }
        ]
      }
    }
  },
  "custom_parameters": {
    "canvas_user_id": "$Canvas.user.id",
    "canvas_course_id": "$Canvas.course.id",
    "canvas_user_email": "$Canvas.user.email"
  }
}
```

**Usage in Canvas:**
1. Go to Canvas Admin → Settings → Apps → View App Configurations
2. Click "+ App" → "By URL"
3. Enter: `https://domain/.well-known/lti-tool-configuration.json`
4. Canvas will automatically fetch and configure the tool

---

## LTI Controller

All endpoints in this controller are prefixed with `/lti`.

### GET `/lti/login`
Initiates the LTI login process.

**Query Parameters:**
- `isLocal` (optional, default: `true`) - Boolean flag to determine if using localhost or public URL

**Response:**
- **Type:** HTML form
- **Action:** Redirects to login form

**Example:**
```bash
curl "https://domain/lti/login?isLocal=false"
```

---

### POST `/lti/login-initiation`
Handles the initial login request from Canvas LTI.

**Request Body:**
- Form data with LTI parameters (typically from Canvas)

**Response:**
- **Type:** Redirect
- **Action:** Redirects to Canvas authorization URL

**Example:**
```bash
curl -X POST https://domain/lti/login-initiation \
  -d "iss=https://canvas.instructure.com" \
  -d "login_hint=..."
```

---

### POST `/lti/launch`
Handles the LTI launch request.

**Request Body:**
- Form data with LTI launch parameters

**Response:**
- **Type:** Redirect
- **Action:** Redirects to the LTI launch URI builder

**Example:**
```bash
curl -X POST https://domain/lti/launch \
  -d "iss=https://canvas.instructure.com" \
  -d "target_link_uri=..."
```

---

### POST `/lti/authorized-redirect`
Handles the authorized redirect after Canvas authentication.

**Request Parameters:**
- `id_token` (optional) - JWT token from Canvas
- Additional LTI parameters as form data

**Response:**
- **Type:** `ResponseEntity<String>`
- **Status:** `302 Found` (redirect)
- **Body:** HTML redirect to frontend URL (`https://lineus-fe.lomtech.net/by_pass_authenticate`)

**Example:**
```bash
curl -X POST https://domain/lti/authorized-redirect \
  -d "id_token=eyJ..." \
  -d "state=..."
```

---


## Security Configuration

All `/lti/**` endpoints and `/error` endpoint are publicly accessible (no authentication required).

---

## Configuration

The application uses the following configuration (from `application.properties`):

- **Server Port:** `9090`
- **Canvas Client IDs:**
  - Chat Room: `10000000000017`
- **LTI Redirect URIs:**
  - Main: `https://calculating-loudly-quinton.ngrok-free.dev/lti/authorized-redirect`
  - Static: `https://calculating-loudly-quinton.ngrok-free.dev/lti/static/authorized-redirect`
- **Canvas URLs:**
  - Issuer: `https://canvas.instructure.com`
  - Canvas Base URL: `https://canvas-lms.lomtech.net` (Authorized URL and JWKS URI are computed from this)
  - Authorized URL: `https://canvas-lms.lomtech.net/api/lti/authorize` (computed from base URL)
  - JWKS URI: `https://canvas-lms.lomtech.net/api/lti/security/jwks` (computed from base URL)

---

## Notes

- All LTI endpoints accept form-encoded data (application/x-www-form-urlencoded)
- The application uses Spring Security with CSRF disabled for LTI endpoints
- Frame options are disabled to allow iframe embedding
- The application logs all LTI requests for debugging purposes

