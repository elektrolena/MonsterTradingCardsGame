CREATE DATABASE mtcgdb;
/*DROP DATABASE mtcgdb;*/

\c mtcgdb;

CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(255) PRIMARY KEY,
    token VARCHAR(255),
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    bio VARCHAR(255),
    image VARCHAR(255)
    );

DELETE FROM users;

DROP TABLE users;

CREATE TABLE IF NOT EXISTS cards (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255),
    element VARCHAR(255),
    damage INT,
    ownerId VARCHAR(255),
    packageId VARCHAR(255)
);

DELETE FROM cards;

DROP TABLE cards;