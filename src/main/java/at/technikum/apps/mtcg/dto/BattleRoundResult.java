package at.technikum.apps.mtcg.dto;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;

public class BattleRoundResult {
    private User winner;
    private User loser;
    private Card winnerCard;
    private Card loserCard;
    private boolean draw;
    private int round;

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    public User getLoser() {
        return loser;
    }

    public void setLoser(User loser) {
        this.loser = loser;
    }

    public Card getWinnerCard() {
        return winnerCard;
    }

    public void setWinnerCard(Card winnerCard) {
        this.winnerCard = winnerCard;
    }

    public Card getLoserCard() {
        return loserCard;
    }

    public void setLoserCard(Card loserCard) {
        this.loserCard = loserCard;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }
}
