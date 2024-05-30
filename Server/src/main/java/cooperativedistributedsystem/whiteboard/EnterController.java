package cooperativedistributedsystem.whiteboard;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class EnterController {

    private final MessageRepository messageRepository;
    private final LockService lockService;
    private int objectId = 0;
    private final int interval = 1024;

    @GetMapping
    public Set<Message> enter() {
        return messageRepository.findAll();
    }

    @GetMapping("id")
    public int allocateObjectId() {
        lockService.addObject(interval);
        return getObjectStartId();
    }

    private synchronized int getObjectStartId() {
        int startId = objectId;
        objectId += interval;
        return startId;
    }

}
