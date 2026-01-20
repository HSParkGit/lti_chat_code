package net.lomtech.lti.service.impl;

import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.lomtech.lti.config.BackendProperties;
import net.lomtech.lti.config.CanvasProperties;
import net.lomtech.lti.config.JWTValidator;
import net.lomtech.lti.network.api.CanvasApis;
import net.lomtech.lti.service.LTIService;
import net.lomtech.lti.util.IframeWrappingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LTIServiceImpl implements LTIService {
    private static final Logger log = LoggerFactory.getLogger(LTIServiceImpl.class);
    
    private final CanvasProperties canvasProperties;
    private final CanvasApis canvasApis;
    private final BackendProperties backendProperties;
    private final RestTemplate restTemplate;

    public LTIServiceImpl(CanvasProperties canvasProperties, CanvasApis canvasApis, 
                         BackendProperties backendProperties, RestTemplate restTemplate) {
        this.canvasProperties = canvasProperties;
        this.canvasApis = canvasApis;
        this.backendProperties = backendProperties;
        this.restTemplate = restTemplate;
    }

    public void getLoginForm(String launchUrl, HttpServletResponse response, Boolean isLocal, String consumerKey) throws IOException {
        response.setContentType("text/html");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>LTI Login</title></head>");
            out.println("<body onload=\"document.forms[0].submit()\">");
            out.println("<form action=\"" + launchUrl + "\" method=\"post\">");
            out.println("    <input type=\"hidden\" name=\"oauthConsumerKey\" value=\"" + consumerKey + "\"/>");
            out.println("    <input type=\"hidden\" name=\"userId\" value=\"1\"/>");
            out.println("    <input type=\"hidden\" name=\"roles\" value=\"admin\"/>");
            out.println("    <input type=\"submit\" value=\"Click to Launch\">");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    public String initialLoginAndGetRedirectURL(Map<String, String> allParams, HttpServletResponse response, String redirectUrl, String clientId) {
        String authorizedUrl = canvasProperties.getAuthorizedUrl();
        log.info("Building Canvas authorization URL from: {}", authorizedUrl);
        log.info("Redirect URI: {}", redirectUrl);
        log.info("Client ID: {}", clientId);
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(authorizedUrl)
            .queryParam("client_id", clientId)
            .queryParam("response_type", "id_token")
            .queryParam("scope", "openid")
            .queryParam("redirect_uri", redirectUrl)
            .queryParam("state", UUID.randomUUID().toString())
            .queryParam("nonce", UUID.randomUUID().toString())
            .queryParam("prompt", "login")
            .queryParam("response_mode", "form_post");
        
        // Add optional parameters if present
        String ltiMessageHint = allParams.get("lti_message_hint");
        if (ltiMessageHint != null && !ltiMessageHint.isEmpty()) {
            builder.queryParam("lti_message_hint", ltiMessageHint);
        }
        
        String loginHint = allParams.get("login_hint");
        if (loginHint != null && !loginHint.isEmpty()) {
            builder.queryParam("login_hint", loginHint);
        }
        
        String finalUrl = builder.build().toUriString();
        log.info("Final Canvas authorization URL: {}", finalUrl);
        return finalUrl;
    }

    public String ltiLaunchURIBuilder(Map<String, String> allParams, String redirectUrl) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(redirectUrl);
            allParams.forEach((key, value) -> builder.queryParam(key, value));
            return builder.build().toUriString();
        } catch (Exception e) {
            log.error("Error during LTI launch: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to build LTI launch URI", e);
        }
    }

    public String getAuthorizedRedirectURL(String idToken, Map<String, String> allParams, String frontendUrl, String clientId) {
        String numericUserId = null;
        String numericCourseId = null;
        Map customClaims = null;
        String locale = "en";
        try {
            JWTClaimsSet claims = JWTValidator.validateJWT(idToken, canvasProperties.getJwksUri(), canvasProperties.getIssuer(), clientId);
            String userId = claims.getSubject();
            customClaims = (Map)claims.getClaim("https://purl.imsglobal.org/spec/lti/claim/custom");
            log.info("custom claim: {}", (Object)(customClaims != null ? customClaims.toString() : "null"));
            
            // Try to get numeric user ID and course ID from custom claims first (highest priority)
            if (customClaims != null) {
                if (customClaims.containsKey("canvas_user_id")) {
                    Object canvasUserIdObj = customClaims.get("canvas_user_id");
                    numericUserId = canvasUserIdObj != null ? String.valueOf(canvasUserIdObj) : null;
                    log.info("Found canvas_user_id in custom claims: {}", numericUserId);
                }
                if (customClaims.containsKey("canvas_course_id")) {
                    Object canvasCourseIdObj = customClaims.get("canvas_course_id");
                    numericCourseId = canvasCourseIdObj != null ? String.valueOf(canvasCourseIdObj) : null;
                    log.info("Found canvas_course_id in custom claims: {}, userId: {}", numericCourseId, numericUserId);
                }
            }
            
            log.info("UserId (sub): {}", (Object)userId);
            Map map = (Map)claims.getClaim("https://purl.imsglobal.org/spec/lti/claim/context");
            String courseId = (String)map.get("id");
            
            // Extract numeric course ID from LTI Advantage service URLs (fallback if not in custom claims)
            if (numericCourseId == null || numericCourseId.isEmpty()) {
                numericCourseId = extractNumericCourseIdFromClaims(claims);
            }
            
            // Extract numeric user ID from LTI Advantage service URLs or other claims (fallback if not in custom claims)
            if (numericUserId == null || numericUserId.isEmpty()) {
                numericUserId = extractNumericUserIdFromClaims(claims);
            }
            
            // Try to get user ID from Names and Roles Provisioning Service (fallback)
            if (numericUserId == null || numericUserId.isEmpty()) {
                Map nrpsService = (Map)claims.getClaim("https://purl.imsglobal.org/spec/lti-nrps/claim/namesrolesservice");
                if (nrpsService != null) {
                    String membershipsUrl = (String)nrpsService.get("context_memberships_url");
                    if (membershipsUrl != null) {
                        log.info("Fetching numeric user ID from Names and Roles service: {}", membershipsUrl);
                        numericUserId = this.canvasApis.getNumericUserIdFromNamesRolesService(idToken, membershipsUrl, userId);
                        if (numericUserId != null && !numericUserId.isEmpty()) {
                            log.info("Successfully extracted numeric user ID from Names and Roles service: {}", numericUserId);
                        }
                    }
                }
            }
            
            // If we still don't have numeric IDs, try to fetch them from Canvas API (last resort)
            // NOTE: Canvas API requires an access token. If API calls fail, configure Canvas to send
            // canvas_user_id and canvas_course_id in custom claims in the LTI tool configuration.
            if (numericUserId == null || numericUserId.isEmpty()) {
                log.info("Fetching numeric user ID from Canvas API for UUID: {}", userId);
                numericUserId = this.canvasApis.getNumericUserId(idToken, userId);
                if (numericUserId == null || numericUserId.isEmpty()) {
                    log.warn("Could not fetch numeric user ID from Canvas API. Please configure Canvas to send 'canvas_user_id' in custom claims.");
                }
            }
            
            // If we still don't have numeric course ID, try to fetch it from Canvas API (last resort)
            if (numericCourseId == null || numericCourseId.isEmpty()) {
                log.info("Fetching numeric course ID from Canvas API for UUID: {}", courseId);
                numericCourseId = this.canvasApis.getNumericCourseId(idToken, courseId);
                if (numericCourseId == null || numericCourseId.isEmpty()) {
                    log.warn("Could not fetch numeric course ID from Canvas API. Please configure Canvas to send 'canvas_course_id' in custom claims.");
                }
            }
            
            // Get locale from launch_presentation
            Map launchPresentation = (Map)claims.getClaim("https://purl.imsglobal.org/spec/lti/claim/launch_presentation");
            if (launchPresentation != null && launchPresentation.containsKey("locale")) {
                Object localeObj = launchPresentation.get("locale");
                locale = localeObj != null ? String.valueOf(localeObj) : "en";
            }
            
            log.info("✅ LTI Launch Success - UserId: {}, CourseId: {}, NumericUserId: {}, NumericCourseId: {}", 
                userId, courseId, numericUserId, numericCourseId);
        }
        catch (ParseException e) {
            log.error("Failed to parse id_token: {}", (Object)e.getMessage());
            throw new RuntimeException("Invalid id_token");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        // Get JWT tokens from chat backend SSO endpoint
        String jwtToken = null;
        String refreshToken = null;
        if (numericUserId != null && !numericUserId.isEmpty()) {
            try {
                Map<String, String> tokens = getJWTTokensFromBackend(numericUserId, customClaims);
                if (tokens != null) {
                    jwtToken = tokens.get("accessToken");
                    refreshToken = tokens.get("refreshToken");
                    log.info("Successfully obtained JWT tokens from chat backend");
                } else {
                    log.warn("getJWTTokensFromBackend returned null");
                }
            } catch (Exception e) {
                log.error("Failed to get JWT tokens from chat backend: {}", e.getMessage());
                // Continue without token - frontend can handle this
            }
        }
        
        // Build query string with JWT tokens and required parameters
        StringBuilder queryStringBuilder = new StringBuilder();
        
        // Add JWT access token (REQUIRED)
        if (jwtToken != null && !jwtToken.isEmpty()) {
            queryStringBuilder.append("token=").append(URLEncoder.encode(jwtToken, StandardCharsets.UTF_8));
        } else {
            log.warn("No JWT access token available for redirect. User will need to authenticate manually.");
        }
        
        // Add JWT refresh token (REQUIRED)
        if (refreshToken != null && !refreshToken.isEmpty()) {
            if (queryStringBuilder.length() > 0) {
                queryStringBuilder.append("&");
            }
            queryStringBuilder.append("refresh_token=").append(URLEncoder.encode(refreshToken, StandardCharsets.UTF_8));
            log.info("Added refresh_token to redirect URL");
        } else {
            log.warn("No JWT refresh token available for redirect.");
        }
        
        // Add course_id (REQUIRED for search API)
        if (numericCourseId != null && !numericCourseId.isEmpty()) {
            if (queryStringBuilder.length() > 0) {
                queryStringBuilder.append("&");
            }
            queryStringBuilder.append("course_id=").append(URLEncoder.encode(numericCourseId, StandardCharsets.UTF_8));
            log.info("Added course_id to redirect URL: {}", numericCourseId);
        } else {
            log.warn("⚠️ WARNING: course_id is missing! Frontend search API will not work without it.");
        }
        
        // Add user ID (Canvas user ID)
        if (numericUserId != null && !numericUserId.isEmpty()) {
            if (queryStringBuilder.length() > 0) {
                queryStringBuilder.append("&");
            }
            queryStringBuilder.append("id=").append(URLEncoder.encode(numericUserId, StandardCharsets.UTF_8));
        }
        
        // Add locale
        if (locale != null && !locale.isEmpty()) {
            if (queryStringBuilder.length() > 0) {
                queryStringBuilder.append("&");
            }
            queryStringBuilder.append("launch_presentation_locale=").append(URLEncoder.encode(locale, StandardCharsets.UTF_8));
        }
        
        String finalUrl;
        if (queryStringBuilder.length() > 0) {
            finalUrl = frontendUrl + "?" + queryStringBuilder.toString();
        } else {
            // If no parameters, redirect without query string (frontend should handle authentication)
            finalUrl = frontendUrl;
        }
        
        log.info("Final redirect URL: {}", finalUrl.replaceAll("token=[^&]*", "token=***").replaceAll("refresh_token=[^&]*", "refresh_token=***"));
        return IframeWrappingUtil.getIframeWrapHTMLStringForLti(finalUrl);
    }
    
    /**
     * Call chat backend SSO endpoint to get JWT tokens (access and refresh)
     * @return Map with "accessToken" and "refreshToken" keys
     */
    private Map<String, String> getJWTTokensFromBackend(String canvasUserId, Map<String, Object> customClaims) {
        try {
            String ssoUrl = backendProperties.getUrl() + "/api/v1/sso/authenticate";
            
            // Build request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("canvasUserId", Long.parseLong(canvasUserId));
            
            // Extract additional info from custom claims if available
            if (customClaims != null) {
                // Handle email - Canvas might send template variable instead of actual email
                if (customClaims.containsKey("canvas_user_email")) {
                    Object emailObj = customClaims.get("canvas_user_email");
                    String email = emailObj != null ? String.valueOf(emailObj) : null;
                    // Check if it's a template variable (starts with $) or actual email
                    if (email != null && !email.startsWith("$") && email.contains("@")) {
                        requestBody.put("email", email);
                    } else if (customClaims.containsKey("canvas_user_login")) {
                        // Use login as email if email is a template variable
                        Object loginObj = customClaims.get("canvas_user_login");
                        String login = loginObj != null ? String.valueOf(loginObj) : null;
                        if (login != null && login.contains("@")) {
                            requestBody.put("email", login);
                        }
                    }
                } else if (customClaims.containsKey("canvas_user_login")) {
                    // Use login as email if email is not provided
                    Object loginObj = customClaims.get("canvas_user_login");
                    String login = loginObj != null ? String.valueOf(loginObj) : null;
                    if (login != null && login.contains("@")) {
                        requestBody.put("email", login);
                    }
                }
                
                // Handle name
                if (customClaims.containsKey("canvas_user_fullname")) {
                    requestBody.put("name", customClaims.get("canvas_user_fullname"));
                } else if (customClaims.containsKey("canvas_user_name")) {
                    requestBody.put("name", customClaims.get("canvas_user_name"));
                }
                
                // Handle login ID
                if (customClaims.containsKey("canvas_user_login")) {
                    requestBody.put("loginId", customClaims.get("canvas_user_login"));
                }
                
                // Extract role from LTI roles claim if available
                // LTI roles are typically in format: "http://purl.imsglobal.org/vocab/lis/v2/membership#Learner"
                // We'll extract the role from custom claims or use default
            }
            
            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            // Call SSO endpoint
            ResponseEntity<Map> response = restTemplate.exchange(
                ssoUrl,
                HttpMethod.POST,
                request,
                Map.class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                
                // Check if response is an error response (has errorCode field)
                if (responseBody.containsKey("errorCode")) {
                    String errorCode = (String) responseBody.get("errorCode");
                    String errorMessage = (String) responseBody.get("errorMessage");
                    log.error("SSO endpoint returned error response: errorCode={}, errorMessage={}", errorCode, errorMessage);
                    log.error("SSO request body was: canvasUserId={}, email={}, name={}", 
                        requestBody.get("canvasUserId"), requestBody.get("email"), requestBody.get("name"));
                    return null;
                }
                
                // Check if response has accessToken and refreshToken (success response)
                if (responseBody.containsKey("accessToken") && responseBody.containsKey("refreshToken")) {
                    String accessToken = (String) responseBody.get("accessToken");
                    String refreshToken = (String) responseBody.get("refreshToken");
                    
                    Map<String, String> tokens = new HashMap<>();
                    if (accessToken != null && !accessToken.isEmpty()) {
                        tokens.put("accessToken", accessToken);
                        log.info("Successfully received JWT access token from chat backend SSO endpoint");
                    } else {
                        log.warn("SSO endpoint returned success but accessToken is null or empty");
                    }
                    
                    if (refreshToken != null && !refreshToken.isEmpty()) {
                        tokens.put("refreshToken", refreshToken);
                        log.info("Successfully received JWT refresh token from chat backend SSO endpoint");
                    } else {
                        log.warn("SSO endpoint returned success but refreshToken is null or empty");
                    }
                    
                    if (tokens.isEmpty()) {
                        return null;
                    }
                    
                    return tokens;
                } else {
                    log.error("SSO endpoint response missing accessToken or refreshToken field. Response keys: {}", responseBody.keySet());
                    return null;
                }
            } else {
                log.warn("SSO endpoint returned non-2xx status: {}", response.getStatusCode());
                return null;
            }
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            log.error("HTTP error calling chat backend SSO endpoint ({}): {}", e.getStatusCode(), e.getMessage());
            if (e.getStatusCode().value() == 404) {
                log.error("SSO endpoint not found. Please verify:");
                log.error("  1. Chat backend is running on port 8080");
                log.error("  2. SSO endpoint is accessible at: {}/api/v1/sso/authenticate", backendProperties.getUrl());
                log.error("  3. Controller is properly registered and component scanned");
            }
            return null;
        } catch (Exception e) {
            log.error("Error calling chat backend SSO endpoint: {}", e.getMessage(), e);
            return null;
        }
    }

    public String getDemoPageHTMLContent(Map<String, String> allParams, HttpServletRequest request) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n")
            .append("<html lang=\"en\">\n")
            .append("<head>\n")
            .append("    <meta charset=\"UTF-8\">\n")
            .append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
            .append("    <title>LTI Launch Demo Page</title>\n")
            .append("    <style>\n")
            .append("        body { font-family: Arial, sans-serif; margin: 20px; }\n")
            .append("        table { width: 100%; border-collapse: collapse; margin-top: 20px; }\n")
            .append("        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n")
            .append("        th { background-color: #f2f2f2; }\n")
            .append("    </style>\n")
            .append("</head>\n")
            .append("<body>\n")
            .append("    <h1>LTI Authorized Redirect Parameters</h1>\n")
            .append("    <p>This page displays all the parameters received during the LTI launch and redirect.</p>\n")
            .append("    <table>\n")
            .append("        <tr><th>Parameter</th><th>Value</th></tr>\n");
        
        allParams.forEach((key, value) -> 
            html.append("        <tr><td>").append(escapeHtml(key)).append("</td><td>").append(escapeHtml(value)).append("</td></tr>\n")
        );
        
        html.append("    </table>\n")
            .append("</body>\n")
            .append("</html>");
        
        return html.toString();
    }
    
    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }

    private String extractNumericCourseIdFromClaims(JWTClaimsSet claims) {
        try {
            Pattern courseIdPattern = Pattern.compile("/courses/(\\d+)/");
            
            // Try to extract from LTI Advantage AGS (Assignment and Grade Services) endpoint
            Map agsEndpoint = (Map)claims.getClaim("https://purl.imsglobal.org/spec/lti-ags/claim/endpoint");
            if (agsEndpoint != null) {
                String lineitemsUrl = (String)agsEndpoint.get("lineitems");
                if (lineitemsUrl != null) {
                    // Pattern: /api/lti/courses/{numeric_id}/line_items
                    Matcher matcher = courseIdPattern.matcher(lineitemsUrl);
                    if (matcher.find()) {
                        String courseId = matcher.group(1);
                        log.info("Extracted numeric course ID from lineitems URL: {}", courseId);
                        return courseId;
                    }
                }
            }
            
            // Try to extract from Names and Roles Provisioning Service endpoint
            Map nrpsService = (Map)claims.getClaim("https://purl.imsglobal.org/spec/lti-nrps/claim/namesrolesservice");
            if (nrpsService != null) {
                String membershipsUrl = (String)nrpsService.get("context_memberships_url");
                if (membershipsUrl != null) {
                    // Pattern: /api/lti/courses/{numeric_id}/names_and_roles
                    Matcher matcher = courseIdPattern.matcher(membershipsUrl);
                    if (matcher.find()) {
                        String courseId = matcher.group(1);
                        log.info("Extracted numeric course ID from namesroles URL: {}", courseId);
                        return courseId;
                    }
                }
            }
            
            // Try to extract from launch_presentation return_url
            Map launchPresentation = (Map)claims.getClaim("https://purl.imsglobal.org/spec/lti/claim/launch_presentation");
            if (launchPresentation != null) {
                String returnUrl = (String)launchPresentation.get("return_url");
                if (returnUrl != null) {
                    // Pattern: /courses/{numeric_id}/external_content/...
                    Matcher matcher = courseIdPattern.matcher(returnUrl);
                    if (matcher.find()) {
                        String courseId = matcher.group(1);
                        log.info("Extracted numeric course ID from return_url: {}", courseId);
                        return courseId;
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Error extracting numeric course ID from claims: {}", (Object)e.getMessage());
        }
        return null;
    }

    private String extractNumericUserIdFromClaims(JWTClaimsSet claims) {
        try {
            // Try to get user ID from Names and Roles Provisioning Service
            Map nrpsService = (Map)claims.getClaim("https://purl.imsglobal.org/spec/lti-nrps/claim/namesrolesservice");
            if (nrpsService != null) {
                String membershipsUrl = (String)nrpsService.get("context_memberships_url");
                if (membershipsUrl != null) {
                    String userSubject = claims.getSubject();
                    log.info("Attempting to fetch numeric user ID from Names and Roles service for subject: {}", userSubject);
                    // Note: This will be called from getAuthorizedRedirectURL where we have access to idToken
                    // We'll need to pass the idToken to make the API call
                    return null; // Will be handled in the main method with idToken
                }
            }
        } catch (Exception e) {
            log.warn("Error extracting numeric user ID from claims: {}", (Object)e.getMessage());
        }
        return null;
    }
}

