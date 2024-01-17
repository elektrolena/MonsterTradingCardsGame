CREATE DATABASE mtcgdb;
/*DROP DATABASE mtcgdb;*/

\c mtcgdb;

CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(255) PRIMARY KEY,
    token VARCHAR(255),
    loginTime timestamp,
    username VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    bio VARCHAR(255),
    image VARCHAR(255),
    coins INT,
    elo INT,
    wins INT,
    losses INT
);

CREATE TABLE IF NOT EXISTS packages (
    id VARCHAR(255) PRIMARY KEY,
    price Int
);

CREATE TABLE IF NOT EXISTS cards (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255),
    element VARCHAR(255),
    type VARCHAR(255),
    damage INT,
    in_deck INT,
    ownerId_fk VARCHAR(255),
    packageId_fk VARCHAR(255),
    FOREIGN KEY (ownerId_fk) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (packageId_fk) REFERENCES packages(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS tradings (
    id VARCHAR(255) PRIMARY KEY,
    userId_fk VARCHAR(255),
    cardId_fk VARCHAR(255),
    desiredType VARCHAR(255),
    desiredDamage INT,
    FOREIGN KEY (cardId_fk) REFERENCES cards(id) ON DELETE CASCADE,
    FOREIGN KEY (userId_fk) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS battles (
    id VARCHAR(255) PRIMARY KEY,
    winnerUsername_fk VARCHAR(255),
    loserUsername_fk VARCHAR(255),
    log VARCHAR,
    is_draw BOOLEAN,
    FOREIGN KEY (winnerUsername_fk) REFERENCES users(username) ON DELETE SET NULL,
    FOREIGN KEY (loserUsername_fk) REFERENCES users(username) ON DELETE SET NULL
);

DELETE FROM users;
DELETE FROM packages;
DELETE FROM cards;
DELETE FROM tradings;
DELETE FROM battles;

DROP TABLE battles;
DROP TABLE tradings;
DROP TABLE cards;
DROP TABLE packages;
DROP TABLE users;