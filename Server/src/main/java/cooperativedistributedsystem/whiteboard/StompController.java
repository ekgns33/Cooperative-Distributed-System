package cooperativedistributedsystem.whiteboard;

import java.util.concurrent.atomic.AtomicBoolean;

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
    private AtomicBoolean loadLock = new AtomicBoolean(false);
    private String loadUserSessionId = "";

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
            if(loadLock.get() && !sessionId.equals(loadUserSessionId)) {
                return;
            }
            messageRepository.save(message);
            sendingOperations.convertAndSend(destination, message);
        }
        else if (message.getStatus() == 4) {
            // send to '/room-user{sessionId}'
            // ex) /room-user13r1sadf13fa
            if(loadLock.get()) {
                return;
            }
            log.info("lock!!. message={}, sessionId={}", message, sessionId);
            message.lockResult = lockService.lock(message.getId());
            MessageHeaders headers = createHeaders(sessionId);
            sendingOperations.convertAndSendToUser(sessionId, destination, message, headers);
        }
        else if (message.getStatus() == 5) {
            if(loadLock.get()) {
                return;
            }
            log.info("unlock!!. message={}", message);
            lockService.unlock(message.getId());
        }
        else if (message.getStatus() == 6) {
            if(!loadLock.compareAndSet(false, true)) {
                message.lockResult = false;
                MessageHeaders headers = createHeaders(sessionId);
                sendingOperations.convertAndSendToUser(sessionId, destination, message, headers);
                return;
            }

            log.info("clear page!!. message = {}", message);
            loadUserSessionId = sessionId;

            MessageHeaders headers = createHeaders(sessionId);
            message.lockResult = true;
            sendingOperations.convertAndSendToUser(sessionId, destination, message, headers);

            message.lockResult = false;
            message.status = 8;
            sendingOperations.convertAndSend(destination, message);

            messageRepository.deleteAll();
            lockService.removeAll();
        }
        else if (message.getStatus() == 7){
            log.info("client ready!!. message ={}", message);
            loadLock.set(false);
            loadUserSessionId = "";
            sendingOperations.convertAndSend(destination, message);
        }
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

}
