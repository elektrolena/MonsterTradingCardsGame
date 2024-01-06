package at.technikum.apps.mtcg.entity;

public class TradingDeal {
    private String id;
    private String userId;
    private String cardId;
    private String desiredType;
    private int desiredDamage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getDesiredType() {
        return desiredType;
    }

    public void setDesiredType(String desiredType) {
        this.desiredType = desiredType;
    }

    public int getDesiredDamage() {
        return desiredDamage;
    }

    public void setDesiredDamage(int desiredDamage) {
        this.desiredDamage = desiredDamage;
    }
}
