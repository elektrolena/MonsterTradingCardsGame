package at.technikum.apps.mtcg.game;

import at.technikum.apps.mtcg.dto.BattleLog;
import at.technikum.apps.mtcg.dto.BattleRoundResult;
import at.technikum.apps.mtcg.entity.Battle;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseBattleRepository;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BattleLogic {
    private BattleLog battleLog;
    private final DatabaseUserRepository databaseUserRepository;
    private final DatabaseBattleRepository databaseBattleRepository;

    public BattleLogic(DatabaseUserRepository databaseUserRepository, DatabaseBattleRepository databaseBattleRepository) {
        this.databaseUserRepository = databaseUserRepository;
        this.databaseBattleRepository = databaseBattleRepository;
    }

    public void setBattleLog(BattleLog battleLog) {
        this.battleLog = battleLog;
    }

    public Battle startBattle(User user1, List<Card> deck1, User user2, List<Card> deck2) {
        int round = 1;
        while(round <= 100 && !deck1.isEmpty() && !deck2.isEmpty()) {
            this.battleLog.addRound(round);
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
            this.battleLog.addRoundResult(result);
        }

        return saveBattle(user1, user2, deck1, deck2);
    }

    private Battle saveBattle(User user1, User user2, List<Card> deck1, List<Card> deck2) {
        Battle battle = new Battle();
        battle.setId(UUID.randomUUID().toString());
        battle.setWinner(user1.getUsername());
        battle.setLoser(user2.getUsername());

        if(!deck1.isEmpty() && !deck2.isEmpty()) {
            battle.setDraw(true);
        } else if(deck1.isEmpty()) {
            battle.setWinner(user2.getUsername());
            user2.setElo(user2.getElo() + 3);
            user2.setWins(user2.getWins() + 1);

            battle.setLoser(user1.getUsername());
            user1.setElo(user1.getElo() - 5);
            user1.setLosses(user1.getLosses() + 1);
        } else {
            user1.setElo(user1.getElo() + 3);
            user1.setWins(user1.getWins() + 1);

            user2.setElo(user2.getElo() - 5);
            user2.setLosses(user2.getLosses() + 1);
        }
        battle.setLog(this.battleLog.getLog());

        this.databaseBattleRepository.save(battle);
        this.databaseUserRepository.update(user1);
        this.databaseUserRepository.update(user2);
        return battle;
    }

    private Card getRandomCard(List<Card> deck) {
        Collections.shuffle(deck);
        return deck.getFirst();
    }

    private double calculateDamage(Card card1, Card card2) {
        String spellElement = card1.getElement();
        String opponentElement = card2.getElement();

        switch (spellElement) {
            case "water":
                if (opponentElement.equals("fire")) {
                    this.battleLog.addString("The damage of the WaterCard was doubled.\n");
                    return card1.getDamage() * 2;
                } else if (opponentElement.equals("normal")) {
                    this.battleLog.addString("The damage of the WaterCard was halved.\n");
                    return card1.getDamage() * 0.5;
                }
                break;
            case "fire":
                if (opponentElement.equals("normal")) {
                    this.battleLog.addString("The damage of the FireCard was doubled.\n");
                    return card1.getDamage() * 2;
                } else if (opponentElement.equals("water")) {
                    this.battleLog.addString("The damage of the FireCard was halved.\n");
                    return card1.getDamage() * 0.5;
                }
                break;
            case "normal":
                if (opponentElement.equals("water")) {
                    this.battleLog.addString("The damage of the RegularCard was doubled.\n");
                    return card1.getDamage() * 2;
                } else if (opponentElement.equals("fire")) {
                    this.battleLog.addString("The damage of the RegularCard was halved.\n");
                    return card1.getDamage() * 0.5;
                }
                break;
        }
        return card1.getDamage();
    }

    private double specialCalculation(Card card1, Card card2) {
        String card1Type = getTypeFromCardName(card1.getName());
        String card2Type = getTypeFromCardName(card2.getName());
        String card2Element = card2.getElement();

        switch (card1Type) {
            case "Goblin":
                if (card2Type.equals("Dragon")) {
                    this.battleLog.addString("The Goblin was too afraid to attack the Dragon, so the Goblin did not deal any damage.\n");
                    return 0;
                }
                break;
            case "Ork":
                if (card2Type.equals("Wizard")) {
                    this.battleLog.addString("The Wizard controlled the Ork, so the Ork did not deal any damage.\n");
                    return 0;
                }
                break;
            case "Knight":
                if (card2Type.equals("Spell") && card2Element.equals("water")) {
                    this.battleLog.addString("The Knight's armor was too heavy so the WaterSpell made it drown and the Knight did not deal any damage.\n");
                    return 0;
                }
                break;
            case "Spell":
                if (card2Type.equals("Kraken")) {
                    this.battleLog.addString("The Kraken is immune against spells, so the Spell did not deal any damage.\n");
                    return 0;
                }
                break;
            case "Dragon":
                if (card2Type.equals("Elv") && card2Element.equals("fire")) {
                    this.battleLog.addString("The FireElv knows Dragons since it was little, so it evaded the Dragon's attack.\n");
                    return 0;
                }
                break;
        }
        return card1.getDamage();
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
