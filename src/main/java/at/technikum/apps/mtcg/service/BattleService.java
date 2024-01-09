package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.game.Battle;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class BattleService {
    private final Battle battle;
    private final CardService cardService;
    private final ConcurrentHashMap<User, List<Card>> queue;
    private final ReentrantLock queueLock;
    private final ScheduledExecutorService scheduler;
    private boolean waitForBattle;

    public BattleService(Battle battle, CardService cardService, ConcurrentHashMap<User, List<Card>> queue, ReentrantLock queueLock) {
        this.battle = battle;
        this.cardService = cardService;
        this.queue = queue;
        this.queueLock = queueLock;
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.waitForBattle = false;
    }

    public synchronized String createBattleLog(User user, UserService userService, List<Card> deck) {
        System.out.println("Test" + user.getUsername());
        if (isUserInQueue(user)) {
            return "You are already in the queue.";
        }

        addUserToQueue(user, deck);
        waitForBattle = true;
        scheduleRemoval(user);

        while(waitForBattle) {
            User otherUser = getOtherUserInQueue();
            if (otherUser != null) {
                removeUserFromQueue(user);
                removeUserFromQueue(otherUser);
                waitForBattle = false;
                return this.battle.startBattle(user, otherUser);
            }
        }

        return "No other user has joined the queue in the last 60 seconds.";
    }

    private boolean isUserInQueue(User user) {
        return queue.containsKey(user);
    }

    private User getOtherUserInQueue() {
        for (Map.Entry<User, List<Card>> entry : queue.entrySet()) {
            User currentUser = entry.getKey();
            if (!currentUser.equals(queue.keySet().iterator().next())) {
                return currentUser;
            }
        }
        return null;
    }

    private void addUserToQueue(User user, List<Card> deck) {
        if (!isUserInQueue(user)) {
            queue.put(user, deck);
        }
    }

    private void scheduleRemoval(User user) {
        scheduler.schedule(() -> {
            try {
                queueLock.lock();
                removeUserFromQueue(user);
            } finally {
                queueLock.unlock();
            }
        }, 60, TimeUnit.SECONDS);
    }

    private void removeUserFromQueue(User user) {
        if(isUserInQueue(user)) {
            queue.remove(user);
            waitForBattle = false;
        }
    }
}
