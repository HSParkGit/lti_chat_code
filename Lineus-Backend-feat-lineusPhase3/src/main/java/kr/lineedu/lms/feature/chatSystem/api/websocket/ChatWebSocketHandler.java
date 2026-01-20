package kr.lineedu.lms.feature.chatSystem.api.websocket;

import kr.lineedu.lms.feature.chatSystem.api.ChatService;
import kr.lineedu.lms.feature.chatSystem.api.dto.request.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatWebSocketHandler {

    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void handleMessage(@Payload SendMessageRequest request) {
        log.info("Received message via WebSocket: {}", request);
        
        try {
            // The service will handle WebSocket broadcasting to all participants
            chatService.sendMessage(request);
            log.info("Message processed successfully");
        } catch (Exception e) {
            log.error("Error handling message: {}", e.getMessage(), e);
            throw e;
        }
    }
}

