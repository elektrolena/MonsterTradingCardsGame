package at.technikum.apps.mtcg.entity;

import java.sql.Timestamp;

public class User {

    private String id;
    private String token;
    private Timestamp loginTimestamp;
    private String username;
    private String name;
    private String password;
    private String bio;
    private String image;
    private int coins;
    private int elo;
    private int wins;
    private int losses;

    public User() {}

    public User(String id, String username, String password, String name, String bio, String image, int coins, int elo, int wins, int losses) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.bio = bio;
        this.image = image;
        this.coins = coins;
        this.elo = elo;
        this.wins = wins;
        this.losses = losses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getLoginTimestamp() {
        return loginTimestamp;
    }

    public void setLoginTimestamp(Timestamp loginTimestamp) {
        this.loginTimestamp = loginTimestamp;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }
}
