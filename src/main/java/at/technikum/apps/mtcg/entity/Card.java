package at.technikum.apps.mtcg.entity;

public class Card {
    private String id;
    private String name;
    private String element;
    private static int damage;
    private String ownerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public static int getDamage() {
        return damage;
    }

    public static void setDamage(int damage) {
        Card.damage = damage;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
