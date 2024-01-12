package at.technikum.apps.mtcg.game;

import at.technikum.apps.mtcg.dto.BattleLog;
import at.technikum.apps.mtcg.dto.BattleRoundResult;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;

import java.util.Collections;
import java.util.List;

public class Battle {
    public String startBattle(User user1, List<Card> deck1, User user2, List<Card> deck2) {

        BattleLog battleLog = new BattleLog();
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

        // TODO: save in database (wins, losses, battle)

        return battleLog.getLog();
    }

    private Card getRandomCard(List<Card> deck) {
        Collections.shuffle(deck);
        return deck.getFirst();
    }

    private static double calculateDamage(Card card1, Card card2) {
        String spellElement = card1.getElement();
        String opponentElement = card2.getElement();

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

    private static double specialCalculation(Card card, Card opponentCard) {
        String cardType = getTypeFromCardName(card.getName());
        String opponentType = getTypeFromCardName(opponentCard.getName());
        String opponentElement = opponentCard.getElement();

        switch (cardType) {
            case "Goblin":
                return "Dragon".equalsIgnoreCase(opponentType) ? 0 : card.getDamage();
            case "Ork":
                return "Wizard".equalsIgnoreCase(opponentType) ? 0 : card.getDamage();
            case "Knight":
                return "Spell".equalsIgnoreCase(opponentType) && "Water".equalsIgnoreCase(opponentElement) ? 0 : card.getDamage();
            case "Spell":
                return "Kraken".equalsIgnoreCase(opponentType) ? 0 : card.getDamage();
            case "Dragon":
                return "Elv".equalsIgnoreCase(opponentType) && "Fire".equalsIgnoreCase(opponentElement) ? 0 : card.getDamage();
            default:
                return card.getDamage();
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
