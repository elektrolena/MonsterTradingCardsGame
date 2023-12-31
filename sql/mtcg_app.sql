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

DELETE FROM users;
DELETE FROM packages;
DELETE FROM cards;
DELETE FROM tradings;

DROP TABLE users;
DROP TABLE packages;
DROP TABLE cards;
DROP TABLE tradings;