package cooperativedistributedsystem.whiteboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StompController {

    private final MessageRepository messageRepository;
    private final SimpMessageSendingOperations sendingOperations;
    private final LockService lockService;
    private final String destination = "/room";

    @MessageMapping("/pub")
    public void sendMessage(Message message, @Header("simpSessionId") String sessionId) {
        if (message.getStatus() == 1) {
            log.info("New user enter!!. message={}", message);
            sendingOperations.convertAndSend(destination, message);
        }
        else if (message.getStatus() == 2) {
            log.info("user out!!. message={}", message);
            sendingOperations.convertAndSend(destination, message);
        }
        else if (message.getStatus() == 3) {
            log.info("message={}", message);
            messageRepository.save(message);
            sendingOperations.convertAndSend(destination, message);
        }
        else if (message.getStatus() == 4) {
            // send to '/room-user{sessionId}'
            // ex) /room-user13r1sadf13fa
            log.info("lock!!. message={}, sessionId={}", message, sessionId);
            message.lockResult = lockService.lock(message.getId());
            MessageHeaders headers = createHeaders(sessionId);
            sendingOperations.convertAndSendToUser(sessionId, destination, message, headers);
        }
        else if (message.getStatus() == 5) {
            log.info("unlock!!. message={}", message);
            lockService.unlock(message.getId());
        }
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

}
