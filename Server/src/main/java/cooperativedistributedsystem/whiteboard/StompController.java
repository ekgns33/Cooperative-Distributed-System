package cooperativedistributedsystem.whiteboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StompController {

    private final MessageRepository messageRepository;
    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/pub")
    public void sendMessage(Message message) {
        if (message.getStatus() == 1) {
            log.info("New user enter!!. message={}", message);
            sendingOperations.convertAndSend("/room", message);
        }
        else if (message.getStatus() == 2) {
            log.info("user out!!. message={}", message);
            sendingOperations.convertAndSend("/room", message);
        }
        else {
            log.info("message={}", message);
            messageRepository.save(message);
            sendingOperations.convertAndSend("/room", message);
        }
    }

}
