package at.technikum.apps.mtcg.entity;

public class Package {
    private String id;
    private int price;
    private Card[] cards;

    public Package() {}

    public Package(String id, int price, Card[] cards) {
        this.id = id;
        this.price = price;
        this.cards = cards;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }
}
