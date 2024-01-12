package at.technikum.apps.mtcg.dto;

public class BattleLog {
    private String log;

    public String getLog() {
        return log;
    }

    public void addRound(int round, BattleRoundResult result) {
        if(result.isDraw()) {
            this.log = this.log
                    + round
                    + ". Round:\n"
                    + " +++ DRAW +++\n"
                    + " Player 1:\n"
                    + " Name: " + result.getWinner().getName() + "\n"
                    + " Card: " + result.getWinnerCard().getName() + "\n"
                    + " Damage: " + result.getWinnerCard().getDamage() + "\n"
                    + " Player 2:\n"
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
