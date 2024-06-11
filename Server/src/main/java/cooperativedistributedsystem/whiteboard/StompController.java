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

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StompController {

    private final MessageRepository messageRepository;
    private final SimpMessageSendingOperations sendingOperations;
    private final LockService lockService;
    private final String destination = "/room";
    private final AtomicBoolean loadLock = new AtomicBoolean(false);
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
            log.info("Drawing!!. message={}", message);
            if (loadLock.get() && !sessionId.equals(loadUserSessionId)) {
                log.info("현재 load lock이 걸려있고, 현재 사용자는 load lock을 수행한 사용자가 아닙니다.");
                return;
            }
            messageRepository.save(message);
            sendingOperations.convertAndSend(destination, message);
        }
        else if (message.getStatus() == 4) {
            if (loadLock.get()) {
                log.info("현재 load lock이 걸려있어, lock을 잡을 수 없습니다.");
                return;
            }
            log.info("lock!!. message={}, sessionId={}", message, sessionId);
            message.lockResult = lockService.lock(message.getId());
            MessageHeaders headers = createHeaders(sessionId);
            sendingOperations.convertAndSendToUser(sessionId, destination, message, headers);
        }
        else if (message.getStatus() == 5) {
            if (loadLock.get()) {
                log.info("현재 load lock이 걸려있어, unlock 할 수 없습니다.");
                return;
            }
            log.info("unlock!!. message={}", message);
            lockService.unlock(message.getId());
        }
        else if (message.getStatus() == 6) {
            MessageHeaders headers = createHeaders(sessionId);
            if (!loadLock.compareAndSet(false, true)) {
                log.info("현재 load lock이 걸려있어, clear를 수행할 수 없습니다.");
                message.lockResult = false;
                sendingOperations.convertAndSendToUser(sessionId, destination, message, headers);
                return;
            }

            log.info("clear page!!. message = {}", message);
            loadUserSessionId = sessionId;

            message.lockResult = true;
            sendingOperations.convertAndSendToUser(sessionId, destination, message, headers);

            message.lockResult = false;
            message.status = 8;
            sendingOperations.convertAndSend(destination, message);

            messageRepository.deleteAll();
            lockService.clearLock();
        }
        else if (message.getStatus() == 7) {
            log.info("client ready!!. message ={}", message);
            loadLock.set(false);
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
