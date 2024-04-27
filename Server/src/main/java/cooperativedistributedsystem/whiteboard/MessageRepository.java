package cooperativedistributedsystem.whiteboard;

import jakarta.annotation.PostConstruct;
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
    private final String key = "chatroom";

    @PostConstruct
    public void deleteAll() {
        redisTemplate.opsForList().getOperations().delete(key);
    }

    public void save(Message message) {
        redisTemplate.opsForList().rightPush(key, message);
    }

    public Set<Message> findAll() {
        List<Message> chatroom = redisTemplate.opsForList().range(key, 0, -1).reversed().stream()
                .map(o -> (Message) o)
                .toList();

        Set<Message> chatroomSet = new HashSet<>(chatroom);
        for (int i = chatroom.size() - 1; i >= 0; i--) {
            chatroomSet.add(chatroom.get(i));
        }
        return chatroomSet;
    }

}
