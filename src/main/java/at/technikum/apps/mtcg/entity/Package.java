package at.technikum.apps.mtcg.entity;

import java.util.List;

public class Package {
    private String id;
    private int price;
    private List<Card> cards;

    public Package() {}

    public Package(String id, int price, List<Card> cards) {
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

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
