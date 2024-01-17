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

    public void addRound(int round) {
        this.log = this.log + "~~~~~~~~~~~~~~~ " + round + ". Round: ~~~~~~~~~~~~~~~\n\n";
    }

    public void addString(String string) {
        this.log = this.log + string;
    }

    public void addRoundResult(BattleRoundResult result) {
        if(result.isDraw()) {
            this.log = this.log
                    + " \n +++++ DRAW +++++\n"
                    + " Name: " + result.getWinner().getName() + "\n"
                    + " Card: " + result.getWinnerCard().getName() + "\n"
                    + " Damage: " + result.getWinnerCard().getDamage() + "\n\n"
                    + " +++++ DRAW +++++\n"
                    + " Name: " + result.getLoser().getName() + "\n"
                    + " Card: " + result.getLoserCard().getName() + "\n"
                    + " Damage: " + result.getLoserCard().getDamage() + "\n\n";
        } else {
            this.log = this.log
                    + " \n ***** WINNER *****\n"
                    + " Name: " + result.getWinner().getName() + "\n"
                    + " Card: " + result.getWinnerCard().getName() + "\n"
                    + " Damage: " + result.getWinnerCard().getDamage() + "\n\n"
                    + " ----- LOSER -----\n"
                    + " Name: " + result.getLoser().getName() + "\n"
                    + " Card: " + result.getLoserCard().getName() + "\n"
                    + " Damage: " + result.getLoserCard().getDamage() + "\n\n";
        }
    }
}
