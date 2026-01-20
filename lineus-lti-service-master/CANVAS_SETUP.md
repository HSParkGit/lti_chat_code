# Canvas LTI Tool Setup Guide

This guide provides step-by-step instructions for configuring the Lineus LTI Tool in Canvas LMS using two methods:
1. **Manual Entry** - Configure each field individually
2. **Paste JSON** - Use a JSON configuration file

---

## Prerequisites

Before starting, ensure you have:
- Admin access to your Canvas LMS instance
- The base URL of your LTI tool deployment (e.g., `https://your-domain.com` or `https://your-ngrok-url.ngrok-free.dev`)
- Access to Canvas Developer Keys section

---

## Method 1: Manual Entry

This method allows you to configure the LTI tool by filling in each field manually in Canvas.

> **Note**: The example values shown below are based on an actual Canvas configuration. Replace the example URLs and email addresses with your own values.

### Step 1: Navigate to Developer Keys

1. Log in to your Canvas LMS instance as an administrator
2. Navigate to **Admin** → **Developer Keys** (or go directly to `/accounts/1/developer_keys`)
3. Click the **"+ Developer Key"** button
4. Select **"+ LTI Key"**

### Step 2: Configure Key Settings

In the **"Key settings"** section (left column), fill in the following:

#### Basic Information

- **Key name**: 
  ```
  Chatting LTI Tool
  ```
  (or any descriptive name you prefer)

- **Owner email**: 
  ```
  venzee@lomtech.net
  ```
  (your email address)

- **Redirect URIs** (required, marked with *):
  ```
  https://calculating-loudly-quinton.ngrok-free.dev/lti/authorized-redirect
  ```
  > **Note**: Replace with your actual domain or ngrok URL. This is computed from `LTI_BASE_URL` configured in your application as `{LTI_BASE_URL}/lti/authorized-redirect`.

- **Notes** (optional):
  ```
  Testing Chatting LTI
  ```
  (any additional notes you want to add)

### Step 3: Configure LTI Settings

In the **"Configure"** section (right column), set the following:

#### Method Selection

- **Method**: Select **"Manual entry"** from the dropdown

#### Required Values

- **Title** (required, marked with *):
  ```
  Chatting LTI Tool
  ```

- **Description** (required, marked with *):
  ```
  Testing Chatting LTI
  ```

- **Target Link URI** (required, marked with *):
  ```
  https://calculating-loudly-quinton.ngrok-free.dev/lti/launch
  ```
  > **Note**: Replace with your actual domain or ngrok URL.

- **OpenID Connect Initiation URL** (required, marked with *):
  ```
  https://calculating-loudly-quinton.ngrok-free.dev/lti/login-initiation
  ```
  > **Note**: Replace with your actual domain or ngrok URL.

#### JWK Configuration

- **JWK Method**: Select **"Public JWK URL"** from the dropdown

- **Public JWK URL**:
  ```
  https://canvas-lms.lomtech.net/api/lti/security/jwks
  ```
  > **Note**: This is the Canvas LMS JWKS endpoint. In some configurations, you may use your tool's own JWKS endpoint at `https://your-domain.com/.well-known/jwks.json` instead. Check your Canvas instance configuration requirements.

### Step 4: Configure LTI Advantage Services (Optional)

1. Expand the **"> LTI Advantage Services"** section if you need additional services
2. Configure any required scopes:
   - Assignment and Grade Services (AGS)
   - Names and Role Provisioning Services (NRPS)
   - Deep Linking

### Step 5: Configure Placements (Optional)

1. In the **Placements** section, select where you want the tool to appear:
   - **Course Navigation** - Adds a link in the course navigation menu
   - **Assignment Menu** - Adds the tool to assignment submission options
   - **Assignment Selection** - Allows selection in assignment configuration

2. For each placement, you can configure:
   - Custom text
   - Icon URL (e.g., `https://your-domain.com/get-image/chatroom-demo.png`)
   - Target Link URI

### Step 6: Save Configuration

1. Review all your settings
2. Click the **"Save"** button (blue button in the bottom right)
3. Canvas will generate a **Client ID** for your tool
4. **Important**: Copy the Client ID and update your application's `application.yml` or environment variables:
   ```yaml
   canvas:
     client-ids:
       chat-room: YOUR_CLIENT_ID_HERE
   ```

### Step 7: Test the Configuration

1. Navigate to a course in Canvas
2. If you configured Course Navigation placement, you should see the tool in the course navigation menu
3. Click on the tool to test the LTI launch

---

## Method 2: Paste JSON

This method allows you to configure the LTI tool by pasting a JSON configuration directly into Canvas.

### Step 1: Navigate to Developer Keys

1. Log in to your Canvas LMS instance as an administrator
2. Navigate to **Admin** → **Developer Keys** (or go directly to `/accounts/1/developer_keys`)
3. Click the **"+ Developer Key"** button
4. Select **"+ LTI Key"**

### Step 2: Configure Key Settings

In the **"Key settings"** section (left column), fill in the following:

#### Basic Information

- **Key name**: 
  ```
  Lineus LTI Tool
  ```
  (or any descriptive name you prefer)

- **Owner email**: 
  ```
  your-email@example.com
  ```
  (your email address)

- **Redirect URIs** (required, marked with *):
  ```
  https://your-domain.com/lti/authorized-redirect
  ```
  > **Note**: Replace `your-domain.com` with your actual domain or ngrok URL. This is computed from `LTI_BASE_URL` configured in your application as `{LTI_BASE_URL}/lti/authorized-redirect`.

- **Notes** (optional):
  ```
  Lineus LTI Tool for course integration
  ```
  (any additional notes you want to add)

### Step 3: Configure LTI Settings with JSON

In the **"Configure"** section (right column):

#### Method Selection

- **Method**: Select **"Paste JSON"** from the dropdown

#### JSON Configuration

Paste the following JSON configuration into the text area:

```json
{
  "title": "Lineus LTI Tool",
  "scopes": [
    "https://purl.imsglobal.org/spec/lti-ags/scope/lineitem",
    "https://purl.imsglobal.org/spec/lti-ags/scope/result.readonly",
    "https://purl.imsglobal.org/spec/lti-ags/scope/score",
    "https://purl.imsglobal.org/spec/lti-nrps/scope/contextmembership.readonly"
  ],
  "extensions": [
    {
      "domain": "your-domain.com",
      "tool_id": "lineus-lti-tool",
      "platform": "canvas.instructure.com",
      "settings": {
        "text": "Lineus LTI Tool",
        "icon_url": "https://your-domain.com/get-image/chatroom-demo.png",
        "placements": [
          {
            "placement": "course_navigation",
            "message_type": "LtiResourceLinkRequest",
            "target_link_uri": "https://your-domain.com/lti/launch",
            "text": "Lineus",
            "icon_url": "https://your-domain.com/get-image/chatroom-demo.png"
          },
          {
            "placement": "assignment_menu",
            "message_type": "LtiResourceLinkRequest",
            "target_link_uri": "https://your-domain.com/lti/launch"
          },
          {
            "placement": "assignment_selection",
            "message_type": "LtiResourceLinkRequest",
            "target_link_uri": "https://your-domain.com/lti/launch"
          }
        ]
      }
    }
  ]
}
```

> **Important**: Replace all instances of `your-domain.com` with your actual domain or ngrok URL.

#### JSON Configuration Explained

- **title**: The display name of the tool
- **scopes**: OAuth 2.0 scopes that define what the tool can access:
  - `lineitem`: Create and manage gradebook line items
  - `result.readonly`: Read assignment results
  - `score`: Submit scores to the gradebook
  - `contextmembership.readonly`: Read course roster information
- **extensions**: Canvas-specific configuration:
  - **domain**: Your tool's domain
  - **tool_id**: Unique identifier for the tool
  - **platform**: Canvas platform identifier
  - **settings**: Tool settings including placements

#### Prettify JSON (Optional)

1. Click the **"Prettify JSON"** button to format the JSON for better readability
2. Verify the JSON is valid before saving

### Step 4: Additional Configuration

After pasting the JSON, Canvas will automatically populate the following fields in the "Configure" section:

- **Title**: Lineus LTI Tool
- **Target Link URI**: `https://your-domain.com/lti/launch`
- **OpenID Connect Initiation URL**: You may need to manually add this:
  ```
  https://your-domain.com/lti/login-initiation
  ```
- **Public JWK URL**: You may need to manually add this:
  ```
  https://your-domain.com/.well-known/jwks.json
  ```

### Step 5: Save Configuration

1. Review all your settings
2. Ensure the JSON is valid (no syntax errors)
3. Click the **"Save"** button (blue button in the bottom right)
4. Canvas will generate a **Client ID** for your tool
5. **Important**: Copy the Client ID and update your application's `application.yml` or environment variables:
   ```yaml
   canvas:
     client-ids:
       chat-room: YOUR_CLIENT_ID_HERE
   ```

### Step 6: Test the Configuration

1. Navigate to a course in Canvas
2. If you configured Course Navigation placement, you should see the tool in the course navigation menu
3. Click on the tool to test the LTI launch

---

## Alternative: Using Configuration URL (Auto-Configuration)

Canvas also supports auto-configuration using a configuration URL. This method automatically fetches the configuration from your LTI tool.

### Step 1: Navigate to Developer Keys

1. Log in to your Canvas LMS instance as an administrator
2. Navigate to **Admin** → **Developer Keys**
3. Click the **"+ Developer Key"** button
4. Select **"+ LTI Key"**

### Step 2: Use Configuration URL

1. In the **"Configure"** section, select **"Enter URL"** from the Method dropdown
2. Enter the configuration URL:
   ```
   https://your-domain.com/.well-known/lti-tool-configuration.json
   ```
3. Click **"Fetch"** or **"Load"** to retrieve the configuration
4. Canvas will automatically populate all fields from the JSON configuration

### Step 3: Complete Key Settings

1. Fill in the **Key settings** section:
   - **Key name**: Lineus LTI Tool
   - **Owner email**: your-email@example.com
   - **Redirect URIs**: `https://your-domain.com/lti/authorized-redirect`

### Step 4: Save and Test

1. Click **"Save"**
2. Copy the generated Client ID
3. Update your application configuration
4. Test the tool in a course

---

## Configuration Reference

### Required Endpoints

Your LTI tool must expose the following endpoints:

| Endpoint | Purpose | Example |
|----------|---------|---------|
| `/lti/login-initiation` | OIDC initiation endpoint | `https://your-domain.com/lti/login-initiation` |
| `/lti/launch` | LTI launch endpoint | `https://your-domain.com/lti/launch` |
| `/lti/authorized-redirect` | OAuth redirect endpoint | `https://your-domain.com/lti/authorized-redirect` |
| `/.well-known/jwks.json` | Public JWK endpoint | `https://your-domain.com/.well-known/jwks.json` |
| `/.well-known/lti-tool-configuration.json` | Tool configuration (optional) | `https://your-domain.com/.well-known/lti-tool-configuration.json` |

### Environment Variables

After obtaining the Client ID from Canvas, update your application configuration:

```yaml
# application.yml
canvas:
  issuer: https://canvas.instructure.com
  base-url: https://your-canvas-instance.com
  client-ids:
    chat-room: YOUR_CLIENT_ID_FROM_CANVAS

lti:
  base-url: https://your-domain.com
```

> **Note**: 
> - Canvas `authorized-url` and `jwks-uri` are automatically computed from Canvas `base-url`:
>   - Authorized URL: `{canvas.base-url}/api/lti/authorize`
>   - JWKS URI: `{canvas.base-url}/api/lti/security/jwks`
> - LTI `redirect-uri` is automatically computed from LTI `base-url`:
>   - Redirect URI: `{lti.base-url}/lti/authorized-redirect`

Or using environment variables:

```bash
export CANVAS_CLIENT_ID_CHAT_ROOM=YOUR_CLIENT_ID_FROM_CANVAS
export LTI_BASE_URL=https://your-domain.com
export CANVAS_BASE_URL=https://your-canvas-instance.com
```

---

## Troubleshooting

### Common Issues

1. **"Invalid redirect URI" error**
   - Ensure the Redirect URI in Canvas exactly matches the computed redirect URI from `LTI_BASE_URL` (i.e., `{LTI_BASE_URL}/lti/authorized-redirect`)
   - Check for trailing slashes and protocol (http vs https)

2. **"JWK not found" error**
   - Verify that `/.well-known/jwks.json` is accessible
   - Check that your application is running and the endpoint is not blocked

3. **"Invalid client ID" error**
   - Ensure the Client ID in your application configuration matches the one generated by Canvas
   - Check that the Client ID is correctly set in environment variables

4. **Tool not appearing in course navigation**
   - Verify that Course Navigation placement is configured
   - Check that the tool is enabled in the course settings
   - Ensure the tool is installed at the account level (not just course level)

5. **Launch fails with authentication error**
   - Verify all URLs are correct and accessible
   - Check that your Canvas instance can reach your LTI tool domain
   - Review application logs for detailed error messages

### Verification Checklist

- [ ] All URLs use HTTPS (required for production)
- [ ] Redirect URI matches exactly in Canvas and application
- [ ] Client ID is correctly configured in application
- [ ] JWK endpoint is accessible: `https://your-domain.com/.well-known/jwks.json`
- [ ] Launch endpoint is accessible: `https://your-domain.com/lti/launch`
- [ ] Login initiation endpoint is accessible: `https://your-domain.com/lti/login-initiation`
- [ ] Tool is enabled in Canvas Developer Keys
- [ ] Tool is installed/enabled in the course or account

---

## Additional Resources

- [Canvas LTI 1.3 Documentation](https://canvas.instructure.com/doc/api/file.lti_dev_key_config.html)
- [IMS Global LTI 1.3 Specification](https://www.imsglobal.org/spec/lti/v1p3/)
- [LTI Advantage Services](https://www.imsglobal.org/activity/learning-tools-interoperability)

---

## Support

For issues or questions:
- Check the application logs for detailed error messages
- Review the [ENDPOINTS.md](ENDPOINTS.md) file for API documentation
- Verify your configuration matches the examples in this guide

