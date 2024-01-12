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
    private final ScheduledExecutorService scheduler;

    public BattleService(Battle battle, CardService cardService, ConcurrentHashMap<User, List<Card>> queue) {
        this.battle = battle;
        this.cardService = cardService;
        this.queue = queue;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public synchronized String createBattleLog(User user, UserService userService, List<Card> deck) {
        if (isUserInQueue(user)) {
            return "You are already in the queue.";
        }

        User otherUser = getOtherUserInQueue();
        if(otherUser != null) {
            List<Card> otherDeck = getOtherUsersDeck();
            notify();
            removeUserFromQueue(user);
            removeUserFromQueue(otherUser);
            return this.battle.startBattle(user, deck, otherUser, otherDeck);
        } else {
            addUserToQueue(user, deck);
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                removeUserFromQueue(user);
                throw new RuntimeException();
            }
        }

        return "Get battle log from database.";
    }

    private boolean isUserInQueue(User user) {
        return queue.containsKey(user);
    }

    private User getOtherUserInQueue() {
        for (Map.Entry<User, List<Card>> entry : queue.entrySet()) {
            return entry.getKey();
        }
        return null;
    }

    private List<Card> getOtherUsersDeck() {
        for (Map.Entry<User, List<Card>> entry : queue.entrySet()) {
            return entry.getValue();
        }
        return null;
    }

    private void addUserToQueue(User user, List<Card> deck) {
        if (!isUserInQueue(user)) {
            queue.put(user, deck);
        }
    }

    private void removeUserFromQueue(User user) {
        if(isUserInQueue(user)) {
            queue.remove(user);
        }
    }
}
