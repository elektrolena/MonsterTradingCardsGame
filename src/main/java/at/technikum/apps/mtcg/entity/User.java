package at.technikum.apps.mtcg.entity;

public class User {

    private String id;
    private String token;
    private String username;
    private String password;
    private String bio;
    private String image;
    private int coins;

    public User() {}

    public User(String id, String username, String password, String bio, String image, int coins) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.image = image;
        this.coins = coins;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
