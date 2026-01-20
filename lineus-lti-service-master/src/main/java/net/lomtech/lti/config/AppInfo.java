package net.lomtech.lti.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Application configuration wrapper for backward compatibility.
 * This class provides a unified interface to access all configuration properties.
 */
@Component
@Getter
@RequiredArgsConstructor
public class AppInfo {
    private final CanvasProperties canvasProperties;
    private final LtiProperties ltiProperties;
    private final FrontendProperties frontendProperties;

    // Backward compatibility methods
    public String getLtiBaseUrl() {
        return ltiProperties.getBaseUrl();
    }

    public String getLtiRedirectUri() {
        return ltiProperties.getRedirectUri();
    }

    public String getLtiStaticRedirectUri() {
        return ltiProperties.getStaticRedirectUri();
    }

    public String getCanvasIssuer() {
        return canvasProperties.getIssuer();
    }

    public String getCanvasClientIdChatRoom() {
        return canvasProperties.getClientIds().getChatRoom();
    }

    public String getCanvasClientIdChatRoomDemo() {
        return canvasProperties.getClientIds().getChatRoomDemo();
    }

    public String getCanvasAuthorizedUrl() {
        return canvasProperties.getAuthorizedUrl();
    }

    public String getCanvasJwksUri() {
        return canvasProperties.getJwksUri();
    }

    public String getCanvasUrlUsing() {
        return canvasProperties.getBaseUrl();
    }
}

