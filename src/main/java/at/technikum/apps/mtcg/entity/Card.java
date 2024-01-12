package at.technikum.apps.mtcg.entity;

public class Card {
    private String id;
    private String name;
    private String element;
    private String type;
    private int damage;
    private int inDeck;
    private String ownerId;
    private String packageId;

    public Card() {}

    public Card(String id) {
        this.id = id;
    }

    public Card(String id, String name, String element, String type, int damage, int inDeck, String ownerId, String packageId) {
        this.id = id;
        this.name = name;
        this.element = element;
        this.type = type;
        this.damage = damage;
        this.inDeck = inDeck;
        this.ownerId = ownerId;
        this.packageId = packageId;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getInDeck() {
        return inDeck;
    }

    public void setInDeck(int inDeck) {
        this.inDeck = inDeck;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public boolean isSpell() {
        return this.type.equals("spell");
    }
}
