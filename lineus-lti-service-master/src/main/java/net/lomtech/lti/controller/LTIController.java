package net.lomtech.lti.controller;

import jakarta.servlet.http.HttpServletResponse;
import net.lomtech.lti.config.AppInfo;
import net.lomtech.lti.config.FrontendProperties;
import net.lomtech.lti.service.LTIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;

/**
 * Controller for handling LTI authentication and launch requests.
 */
@Controller
@RequestMapping("/lti")
public class LTIController {
    private static final Logger log = LoggerFactory.getLogger(LTIController.class);
    private static final String CONSUMER_KEY = "CONSUMER_KEY";
    
    private final AppInfo appInfo;
    private final LTIService ltiService;
    private final FrontendProperties frontendProperties;

    public LTIController(AppInfo appInfo, LTIService ltiService, FrontendProperties frontendProperties) {
        this.appInfo = appInfo;
        this.ltiService = ltiService;
        this.frontendProperties = frontendProperties;
    }

    @GetMapping("/login")
    public void login(HttpServletResponse response, 
                     @RequestParam(value = "isLocal", defaultValue = "true") Boolean isLocal) throws IOException {
        String host = Boolean.TRUE.equals(isLocal) ? "http://localhost:9090" : appInfo.getLtiBaseUrl();
        String launchUrl = host + "/lti/launch";
        ltiService.getLoginForm(launchUrl, response, isLocal, CONSUMER_KEY);
    }

    @PostMapping("/login-initiation")
    public void loginInitiation(@RequestParam Map<String, String> allParams, 
                               HttpServletResponse response) throws IOException {
        log.info("Login initiation - params: {}", allParams);
        
        String ltiRedirectUri = appInfo.getLtiRedirectUri();
        String clientId = appInfo.getCanvasClientIdChatRoom();
        
        log.info("Using redirect URI: {}", ltiRedirectUri);
        log.info("Using client ID: {}", clientId);
        
        try {
            String redirectUrl = ltiService.initialLoginAndGetRedirectURL(
                allParams, 
                response, 
                ltiRedirectUri, 
                clientId
            );
            log.info("Redirecting to Canvas authorization URL: {}", redirectUrl);
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            log.error("Error during login initiation", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to initiate login: " + e.getMessage());
        }
    }

    @PostMapping("/launch")
    public void launch(@RequestParam Map<String, String> allParams, 
                      HttpServletResponse response) throws IOException {
        log.info("LTI launch - params: {}", allParams);
        response.sendRedirect(ltiService.ltiLaunchURIBuilder(allParams, appInfo.getLtiRedirectUri()));
    }

    @PostMapping("/authorized-redirect")
    public ResponseEntity<String> ltiRedirect(@RequestParam(name = "id_token", required = false) String idToken, 
                                             @RequestParam Map<String, String> allParams) {
        log.info("Authorized redirect - received id_token: {}", idToken != null ? "present" : "missing");
        log.debug("Redirect params: {}", allParams);
        return ResponseEntity.status(HttpStatus.FOUND)
            .body(ltiService.getAuthorizedRedirectURL(
                idToken, 
                allParams, 
                frontendProperties.getUrl(), 
                appInfo.getCanvasClientIdChatRoom()
            ));
    }
}

