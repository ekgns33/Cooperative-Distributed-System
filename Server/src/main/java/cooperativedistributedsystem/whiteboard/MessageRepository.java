package cooperativedistributedsystem.whiteboard;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class MessageRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void save(Message message) {
        redisTemplate.opsForList().rightPush("chatroom", message);
    }

    public List<Message> findAll() {
        List<Message> chatroom = redisTemplate.opsForList().range("chatroom", 0, -1).reversed().stream()
                .map(o -> (Message) o)
                .toList();

        Set<Message> chatroomSet = new HashSet<>(chatroom);
        for (int i = chatroom.size() - 1; i >= 0; i--) {
            chatroomSet.add(chatroom.get(i));
        }
        return chatroomSet.stream()
                .sorted((o1, o2) -> (int) (o1.getTime() - o2.getTime()))
                .toList();
    }

}
