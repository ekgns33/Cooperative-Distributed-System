package cooperativedistributedsystem.whiteboard;

import java.util.List;

public record EnterResponse(List<Message> messages, long startId) {
}
