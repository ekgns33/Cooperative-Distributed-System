package cooperativedistributedsystem.whiteboard;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class LockService {

    private final List<AtomicBoolean> locks = new ArrayList<>();

    public synchronized void addObject(int number) {
        for (int i = 0; i < number; i++) {
            locks.add(new AtomicBoolean(false));
        }
    }

    public boolean lock(int objectId) {
        AtomicBoolean lock = locks.get(objectId);
        return lock.compareAndSet(false, true);
    }

    public void unlock(int objectId) {
        AtomicBoolean lock = locks.get(objectId);
        lock.set(false);
    }
}
