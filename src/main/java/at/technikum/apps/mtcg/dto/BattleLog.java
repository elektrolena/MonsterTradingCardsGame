package at.technikum.apps.mtcg.dto;

import at.technikum.apps.mtcg.entity.User;

public class BattleLog {
    private String log;

    public BattleLog(User user1, User user2) {
        this.log = "~~~ Battle started between " + user1.getName() + " and " + user2.getName() + " ~~~\n\n";
    }
    public String getLog() {
        return log;
    }

    public void addRound(int round, BattleRoundResult result) {
        if(result.isDraw()) {
            this.log = this.log
                    + round
                    + ". Round:\n"
                    + " +++ DRAW +++\n"
                    + " Name: " + result.getWinner().getName() + "\n"
                    + " Card: " + result.getWinnerCard().getName() + "\n"
                    + " Damage: " + result.getWinnerCard().getDamage() + "\n"
                    + " +++ DRAW +++\n"
                    + " Name: " + result.getLoser().getName() + "\n"
                    + " Card: " + result.getLoserCard().getName() + "\n"
                    + " Damage: " + result.getLoserCard().getDamage() + "\n\n";
        } else {
            this.log = this.log
                    + round
                    + ". Round:\n"
                    + " *** WINNER ***\n"
                    + " Name: " + result.getWinner().getName() + "\n"
                    + " Card: " + result.getWinnerCard().getName() + "\n"
                    + " Damage: " + result.getWinnerCard().getDamage() + "\n"
                    + " --- LOSER ---\n"
                    + " Name: " + result.getLoser().getName() + "\n"
                    + " Card: " + result.getLoserCard().getName() + "\n"
                    + " Damage: " + result.getLoserCard().getDamage() + "\n\n";
        }
    }
}
