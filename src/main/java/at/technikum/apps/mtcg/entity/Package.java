package at.technikum.apps.mtcg.entity;

import java.lang.reflect.Array;

public class Package {
    private String id;
    private Array cards;

    public Package(String id, Array cards) {
        this.id = id;
        this.cards = cards;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Array getCards() {
        return cards;
    }

    public void setCards(Array cards) {
        this.cards = cards;
    }
}
