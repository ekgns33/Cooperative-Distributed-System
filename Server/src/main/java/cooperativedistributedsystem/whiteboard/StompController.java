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
            log.info("New user enter!!");
            sendingOperations.convertAndSend("/room", "enter " + message.getNickname());
        }
        else if (message.getStatus() == 2) {
            log.info("user out!!");
            sendingOperations.convertAndSend("/room", "out " + message.getNickname());
        }
        else {
            log.info("{}", message);
            messageRepository.save(message);
            sendingOperations.convertAndSend("/room", message);
        }
    }

}