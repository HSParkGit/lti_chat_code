package net.lomtech.lti.network.api;

/**
 * Service for interacting with Canvas LMS API.
 * Provides methods to fetch user and course information from Canvas.
 */

import com.nimbusds.jwt.JWTClaimsSet;
import java.util.Map;
import net.lomtech.lti.config.CanvasProperties;
import net.lomtech.lti.config.JWTValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class CanvasApis {
    private static final Logger log = LoggerFactory.getLogger(CanvasApis.class);
    
    private final RestTemplate restTemplate;
    private final CanvasProperties canvasProperties;

    public CanvasApis(RestTemplate restTemplate, CanvasProperties canvasProperties) {
        this.restTemplate = restTemplate;
        this.canvasProperties = canvasProperties;
    }

    public ResponseEntity<Map<String, Object>> fetchUserInfo(String idToken) {
        try {
            JWTClaimsSet claims = JWTValidator.validateJWT(idToken, canvasProperties.getJwksUri(), canvasProperties.getIssuer(), canvasProperties.getClientIds().getChatRoom());
            String userId = claims.getSubject();
            String canvasApiUrl = canvasProperties.getBaseUrl() + "/api/v1/users/" + userId;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + idToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity entity = new HttpEntity((MultiValueMap)headers);
            ParameterizedTypeReference<Map<String, Object>> typeRef = new ParameterizedTypeReference<Map<String, Object>>() {};
            ResponseEntity<Map<String, Object>> response = this.restTemplate.exchange(canvasApiUrl, HttpMethod.GET, entity, typeRef);
            log.info("User info retrieved successfully");
            return response;
        }
        catch (Exception e) {
            log.error("Error fetching user info: {}", (Object)e.getMessage(), (Object)e);
            throw new RuntimeException("Failed to fetch user information", e);
        }
    }

    public String getNumericUserId(String idToken, String userUuid) {
        try {
            // Try using the idToken as Bearer token
            String canvasApiUrl = canvasProperties.getBaseUrl() + "/api/v1/users/" + userUuid;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + idToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity entity = new HttpEntity((MultiValueMap)headers);
            ParameterizedTypeReference<Map<String, Object>> typeRef = new ParameterizedTypeReference<Map<String, Object>>() {};
            ResponseEntity<Map<String, Object>> response = this.restTemplate.exchange(canvasApiUrl, HttpMethod.GET, entity, typeRef);
            if (response.getBody() != null && response.getBody().containsKey("id")) {
                Object id = response.getBody().get("id");
                return id != null ? String.valueOf(id) : null;
            }
            return null;
        }
        catch (org.springframework.web.client.HttpClientErrorException e) {
            // If 401/403, try using the "self" endpoint which might work with idToken
            if (e.getStatusCode().value() == 401 || e.getStatusCode().value() == 403) {
                try {
                    String canvasApiUrl = canvasProperties.getBaseUrl() + "/api/v1/users/self";
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", "Bearer " + idToken);
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity entity = new HttpEntity((MultiValueMap)headers);
                    ParameterizedTypeReference<Map<String, Object>> typeRef = new ParameterizedTypeReference<Map<String, Object>>() {};
                    ResponseEntity<Map<String, Object>> response = this.restTemplate.exchange(canvasApiUrl, HttpMethod.GET, entity, typeRef);
                    if (response.getBody() != null && response.getBody().containsKey("id")) {
                        Object id = response.getBody().get("id");
                        return id != null ? String.valueOf(id) : null;
                    }
                } catch (Exception e2) {
                    log.warn("Error fetching numeric user ID from /self endpoint: {}", (Object)e2.getMessage());
                }
            }
            log.warn("Error fetching numeric user ID (HTTP {}): {}", (Object)e.getStatusCode().value(), (Object)e.getMessage());
            return null;
        }
        catch (Exception e) {
            log.warn("Error fetching numeric user ID: {}", (Object)e.getMessage(), (Object)e);
            return null;
        }
    }

    public String getNumericCourseId(String idToken, String courseUuid) {
        try {
            // Try using the idToken as Bearer token
            String canvasApiUrl = canvasProperties.getBaseUrl() + "/api/v1/courses/" + courseUuid;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + idToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity entity = new HttpEntity((MultiValueMap)headers);
            ParameterizedTypeReference<Map<String, Object>> typeRef = new ParameterizedTypeReference<Map<String, Object>>() {};
            ResponseEntity<Map<String, Object>> response = this.restTemplate.exchange(canvasApiUrl, HttpMethod.GET, entity, typeRef);
            if (response.getBody() != null && response.getBody().containsKey("id")) {
                Object id = response.getBody().get("id");
                return id != null ? String.valueOf(id) : null;
            }
            return null;
        }
        catch (org.springframework.web.client.HttpClientErrorException e) {
            log.warn("Error fetching numeric course ID (HTTP {}): {}", (Object)e.getStatusCode().value(), (Object)e.getMessage());
            return null;
        }
        catch (Exception e) {
            log.warn("Error fetching numeric course ID: {}", (Object)e.getMessage(), (Object)e);
            return null;
        }
    }

    public String getNumericUserIdFromNamesRolesService(String idToken, String namesRolesUrl, String userSubject) {
        try {
            // Call the Names and Roles Provisioning Service to get user information
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + idToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity entity = new HttpEntity((MultiValueMap)headers);
            ParameterizedTypeReference<Map<String, Object>> typeRef = new ParameterizedTypeReference<Map<String, Object>>() {};
            ResponseEntity<Map<String, Object>> response = this.restTemplate.exchange(namesRolesUrl, HttpMethod.GET, entity, typeRef);
            
            log.info("Names and Roles service response status: {}", response.getStatusCode());
            
            if (response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                log.info("Names and Roles service response body keys: {}", body.keySet());
                
                // The response should have a "members" array
                if (body.containsKey("members") && body.get("members") instanceof java.util.List) {
                    java.util.List members = (java.util.List)body.get("members");
                    log.info("Found {} members in Names and Roles service response", members.size());
                    
                    // Find the member that matches the user subject (LTI 1.3 sub claim)
                    for (Object memberObj : members) {
                        if (memberObj instanceof Map) {
                            Map member = (Map)memberObj;
                            log.info("Member data: {}", member);
                            
                            // Check if this member's user_id matches the subject
                            if (member.containsKey("user_id") && userSubject.equals(String.valueOf(member.get("user_id")))) {
                                // Canvas Names and Roles service might include additional fields
                                // Check for canvas_user_id, lis_person_sourcedid, or other numeric ID fields
                                if (member.containsKey("canvas_user_id")) {
                                    Object canvasUserId = member.get("canvas_user_id");
                                    if (canvasUserId != null) {
                                        log.info("Found canvas_user_id in member: {}", canvasUserId);
                                        return String.valueOf(canvasUserId);
                                    }
                                }
                                
                                // Check for lis_person_sourcedid which sometimes contains numeric ID
                                if (member.containsKey("lis_person_sourcedid")) {
                                    Object sourcedId = member.get("lis_person_sourcedid");
                                    if (sourcedId != null) {
                                        String sourcedIdStr = String.valueOf(sourcedId);
                                        // If it's numeric, it might be the Canvas user ID
                                        try {
                                            Long.parseLong(sourcedIdStr);
                                            log.info("Found numeric lis_person_sourcedid: {}", sourcedIdStr);
                                            return sourcedIdStr;
                                        } catch (NumberFormatException e) {
                                            // Not numeric, continue
                                        }
                                    }
                                }
                                
                                log.info("Found matching user in Names and Roles service, but no numeric ID found in member data");
                            }
                        }
                    }
                }
            }
            return null;
        }
        catch (org.springframework.web.client.HttpClientErrorException e) {
            log.warn("Error fetching user ID from Names and Roles service (HTTP {}): {}", (Object)e.getStatusCode().value(), (Object)e.getMessage());
            return null;
        }
        catch (Exception e) {
            log.warn("Error fetching user ID from Names and Roles service: {}", (Object)e.getMessage(), (Object)e);
            return null;
        }
    }
}

