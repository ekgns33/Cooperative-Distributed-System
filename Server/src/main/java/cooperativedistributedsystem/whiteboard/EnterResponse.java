package cooperativedistributedsystem.whiteboard;

import java.util.Set;

public record EnterResponse(Set<Message> messages, long startId) {
}
