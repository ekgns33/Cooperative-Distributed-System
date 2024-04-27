package cooperativedistributedsystem.whiteboard;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class EnterController {

    private final MessageRepository messageRepository;
    private long objectId = 0L;
    private final long interval = 1024L;

    @GetMapping
    public Set<Message> enter() {
        return messageRepository.findAll();
    }

    @GetMapping("id")
    public long allocateObjectId() {
        return getObjectStartId();
    }

    private synchronized long getObjectStartId() {
        long startId = objectId;
        objectId += interval;
        return startId;
    }

}
