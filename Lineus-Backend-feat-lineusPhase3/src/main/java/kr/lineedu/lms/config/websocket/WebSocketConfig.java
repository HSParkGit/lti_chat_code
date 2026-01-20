package kr.lineedu.lms.config.websocket;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@ConditionalOnBean(name = "attendanceProgressWebSocketHandler")
public class WebSocketConfig implements WebSocketConfigurer {

    // This class is conditionally loaded only if AttendanceProgressWebSocketHandler bean exists
    // The handler should be defined in the panopto feature package when available
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Handler registration moved to feature-specific configuration
        // This prevents compilation errors when feature classes are not available
    }
}

