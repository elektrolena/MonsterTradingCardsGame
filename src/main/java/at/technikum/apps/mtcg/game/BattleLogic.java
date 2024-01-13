package at.technikum.apps.mtcg.game;

import at.technikum.apps.mtcg.dto.BattleLog;
import at.technikum.apps.mtcg.dto.BattleRoundResult;
import at.technikum.apps.mtcg.entity.Battle;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseBattleRepository;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BattleLogic {

    private final DatabaseBattleRepository databaseBattleRepository;

    public BattleLogic(DatabaseBattleRepository databaseBattleRepository) {
        this.databaseBattleRepository = databaseBattleRepository;
    }

    public Battle startBattle(User user1, List<Card> deck1, User user2, List<Card> deck2) {
        BattleLog battleLog = new BattleLog(user1, user2);
        int round = 0;
        while(round < 100 && !deck1.isEmpty() && !deck2.isEmpty()) {
            Card card1 = getRandomCard(deck1);
            Card card2 = getRandomCard(deck2);

            double damage1 = specialCalculation(card1, card2);
            double damage2 = specialCalculation(card2, card1);

            damage1 = card1.isSpell() ? calculateDamage(card1, card2) : damage1;
            damage2 = card2.isSpell() ? calculateDamage(card2, card1) : damage2;

            BattleRoundResult result = new BattleRoundResult();

            if (damage1 > damage2) {
                result.setWinner(user1);
                result.setLoser(user2);
                result.setWinnerCard(card1);
                result.setLoserCard(card2);
                deck2.remove(card2);
                deck1.add(card2);
            } else if (damage2 > damage1) {
                result.setWinner(user2);
                result.setLoser(user1);
                result.setWinnerCard(card2);
                result.setLoserCard(card1);
                deck1.remove(card1);
                deck2.add(card1);
            } else {
                result.setDraw(true);
                result.setWinner(user1);
                result.setLoser(user2);
                result.setWinnerCard(card1);
                result.setLoserCard(card2);
            }
            round++;
            battleLog.addRound(round, result);
        }

        Battle battle = new Battle();
        battle.setId(UUID.randomUUID().toString());
        battle.setWinner(user1.getUsername());
        battle.setLoser(user2.getUsername());
        if(!deck1.isEmpty() && !deck2.isEmpty()) {
            battle.setDraw(true);
        } else if(deck1.isEmpty()) {
            battle.setWinner(user2.getUsername());
            battle.setLoser(user1.getUsername());
        }
        battle.setLog(battleLog.getLog());
        // TODO: save in database (wins, losses, battle)
        this.databaseBattleRepository.save(battle);

        return battle;
    }

    private Card getRandomCard(List<Card> deck) {
        Collections.shuffle(deck);
        return deck.getFirst();
    }

    private static double calculateDamage(Card card1, Card card2) {
        String spellElement = card1.getElement();
        String opponentElement = card2.getElement();

        // TODO: add this to log
        switch (spellElement) {
            case "Water":
                if ("Fire".equalsIgnoreCase(opponentElement)) {
                    return card1.getDamage() * 2;
                } else if ("Regular".equalsIgnoreCase(opponentElement)) {
                    return card1.getDamage() * 0.5;
                } else {
                    return card1.getDamage();
                }
            case "Fire":
                if ("Regular".equalsIgnoreCase(opponentElement)) {
                    return card1.getDamage() * 2;
                } else if ("Water".equalsIgnoreCase(opponentElement)) {
                    return card1.getDamage() * 0.5;
                } else {
                    return card1.getDamage();
                }
            case "Regular":
                if ("Water".equalsIgnoreCase(opponentElement)) {
                    return card1.getDamage() * 2;
                } else if ("Fire".equalsIgnoreCase(opponentElement)) {
                    return card1.getDamage() * 0.5;
                } else {
                    return card1.getDamage();
                }
            default:
                return card1.getDamage();
        }
    }

    private static double specialCalculation(Card card1, Card card2) {
        String card1Type = getTypeFromCardName(card1.getName());
        String card2Type = getTypeFromCardName(card2.getName());
        String card2Element = card2.getElement();

        // TODO: add this to log
        switch (card1Type) {
            case "Goblin":
                return "Dragon".equalsIgnoreCase(card2Type) ? 0 : card1.getDamage();
            case "Ork":
                return "Wizard".equalsIgnoreCase(card2Type) ? 0 : card1.getDamage();
            case "Knight":
                return "Spell".equalsIgnoreCase(card2Type) && "Water".equalsIgnoreCase(card2Element) ? 0 : card1.getDamage();
            case "Spell":
                return "Kraken".equalsIgnoreCase(card2Type) ? 0 : card1.getDamage();
            case "Dragon":
                return "Elv".equalsIgnoreCase(card2Type) && "Fire".equalsIgnoreCase(card2Element) ? 0 : card1.getDamage();
            default:
                return card1.getDamage();
        }
    }

    public static String getTypeFromCardName(String cardName) {
        String[] types = {"Knight", "Goblin", "Dragon", "Ork", "Wizard", "Elv", "Spell", "Kraken"};

        for (String type : types) {
            if (cardName.toLowerCase().contains(type.toLowerCase())) {
                return type;
            }
        }

        return "Unknown";
    }
}
