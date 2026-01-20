# Lineus LTI Application

A Spring Boot application for handling LTI (Learning Tools Interoperability) launches from Canvas LMS.

## Features

- LTI 1.3 authentication and authorization
- Canvas LMS integration
- Support for multiple LTI tool configurations
- Static demo page for testing
- RESTful API endpoints

## Prerequisites

- Java 17 or higher
- Gradle 7.x or higher
- Canvas LMS instance with LTI tool configured

## Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Lineus-LTI
   ```

2. **Create environment file**
   ```bash
   cp .env.example .env
   ```

3. **Configure environment variables**
   Edit `.env` file with your configuration:
   ```env
   SERVER_PORT=9090
   CANVAS_ISSUER=https://canvas.instructure.com
   CANVAS_BASE_URL=https://your-canvas-instance.com
   CANVAS_CLIENT_ID_CHAT_ROOM=your_client_id
   LTI_BASE_URL=https://your-domain.com
   FRONTEND_BASE_URL=https://your-frontend.com
   ```

4. **Build the application**
   ```bash
   ./gradlew build
   ```

5. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

## Configuration

The application uses environment variables for configuration. See `.env.example` for all available configuration options.

### Key Configuration Properties

- `SERVER_PORT`: Port on which the application runs (default: 9090)
- `CANVAS_BASE_URL`: Base URL of your Canvas LMS instance
- `CANVAS_CLIENT_ID_CHAT_ROOM`: Canvas client ID for chat room tool
- `CANVAS_CLIENT_ID_CHAT_ROOM_DEMO`: Canvas client ID for demo tool
- `LTI_BASE_URL`: Base URL of your LTI tool (redirect URI is computed as `{LTI_BASE_URL}/lti/authorized-redirect`)
- `FRONTEND_BASE_URL`: Base URL of your frontend application (full URL is computed as `{FRONTEND_BASE_URL}/by_pass_authenticate`)

## API Endpoints

See [ENDPOINTS.md](ENDPOINTS.md) for detailed API documentation.

## Development

### Setting Up Environment

1. **Create environment file:**
   ```bash
   # For development
   ./scripts/setup-env.sh dev
   
   # For production
   ./scripts/setup-env.sh prod
   ```

2. **Edit `.env` file** with your actual values

3. **Validate environment:**
   ```bash
   ./scripts/validate-env.sh
   ```

### Running with ngrok (for local development)

1. Start ngrok:
   ```bash
   ngrok http 9090 --domain=domain
   ```

2. Update `.env` with ngrok URL:
   ```env
   LTI_BASE_URL=https://domain
   ```

3. Update Canvas LTI tool configuration with the ngrok redirect URI


## Project Structure

```
src/main/java/net/lomtech/helloworld/lti/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── service/         # Business logic
├── network/         # External API clients
└── util/            # Utility classes
```

