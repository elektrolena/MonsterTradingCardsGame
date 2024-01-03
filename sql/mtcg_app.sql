CREATE DATABASE mtcgdb;
/*DROP DATABASE mtcgdb;*/

\c mtcgdb;

CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(255) PRIMARY KEY,
    token VARCHAR(255),
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    bio VARCHAR(255),
    image VARCHAR(255),
    coins INT
    );

CREATE TABLE IF NOT EXISTS packages (
    id VARCHAR(255) PRIMARY KEY,
    price Int
);

CREATE TABLE IF NOT EXISTS cards (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255),
    element VARCHAR(255),
    damage INT,
    ownerId_fk VARCHAR(255),
    packageId_fk VARCHAR(255),
    FOREIGN KEY (ownerId_fk) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (packageId_fk) REFERENCES packages(id) ON DELETE SET NULL
);

/* TODO: change DELETE so decks are deleted when user/cards are */

CREATE TABLE IF NOT EXISTS decks (
    id VARCHAR(255) PRIMARY KEY,
    ownerId_fk VARCHAR(255) NOT NULL,
    FOREIGN KEY (ownerId_fk) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS deck_cards (
    deckId_fk VARCHAR(255),
    cardId_fk VARCHAR(255),
    PRIMARY KEY (deckId_fk, cardId_fk),
    FOREIGN KEY (deckId_fk) REFERENCES decks(id) ON DELETE CASCADE,
    FOREIGN KEY (cardId_fk) REFERENCES cards(id) ON DELETE CASCADE
);

DELETE FROM users;
DELETE FROM packages;
DELETE FROM cards;
DELETE FROM decks;
DELETE FROM deck_cards;

DROP TABLE users;
DROP TABLE packages;
DROP TABLE cards;
DROP TABLE decks;
DROP TABLE deck_cards;