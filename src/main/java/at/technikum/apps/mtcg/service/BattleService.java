package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.dto.BattleLog;
import at.technikum.apps.mtcg.entity.Battle;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.exceptions.ExceptionMessage;
import at.technikum.apps.mtcg.exceptions.HttpStatusException;
import at.technikum.apps.mtcg.game.BattleLogic;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class BattleService {
    private final AuthorizationService authorizationService;
    private final DeckService deckService;
    private Battle battle;
    private final BattleLogic battleLogic;
    private final ConcurrentHashMap<User, List<Card>> queue;

    public BattleService(AuthorizationService authorizationService, DeckService deckService, Battle battle, BattleLogic battleLogic, ConcurrentHashMap<User, List<Card>> queue) {
        this.authorizationService = authorizationService;
        this.deckService = deckService;
        this.battle = battle;
        this.battleLogic = battleLogic;
        this.queue = queue;
    }

    public synchronized String createBattleLog(String authorizationToken) {
        User user = this.authorizationService.getLoggedInUser(authorizationToken);

        List<Card> deck = this.deckService.getDeck(authorizationToken);
        if (deck.isEmpty()) {
            throw new HttpStatusException(HttpStatus.NO_CONTENT, HttpContentType.TEXT_PLAIN, ExceptionMessage.NO_CONTENT_DECK);
        }

        if (isUserInQueue(user)) {
            return "You are already in the queue.";
        }

        User otherUser = getOtherUserInQueue();
        if(otherUser != null) {
            List<Card> otherDeck = getOtherUsersDeck();
            removeUserFromQueue(user);
            removeUserFromQueue(otherUser);
            this.battleLogic.setBattleLog(new BattleLog(user, otherUser));
            this.battle = this.battleLogic.startBattle(user, deck, otherUser, otherDeck);
            notify();
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
        return this.battle.getLog();
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
