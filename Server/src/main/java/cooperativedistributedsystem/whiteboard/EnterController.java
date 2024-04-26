package cooperativedistributedsystem.whiteboard;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EnterController {

    private final MessageRepository messageRepository;
    private long objectId = 0L;
    private final long interval = 1000L;

    @GetMapping
    public EnterResponse enter() {
        List<Message> all = messageRepository.findAll();
        long startId = getObjectStartId();
        return new EnterResponse(all, startId);
    }

    @PostMapping
    public long allocateObjectId() {
        return getObjectStartId();
    }

    private synchronized long getObjectStartId() {
        long startId = objectId;
        objectId += interval;
        return startId;
    }

}
