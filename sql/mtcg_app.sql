CREATE DATABASE mtcgdb;
/*DROP DATABASE mtcgdb;*/

\c mtcgdb;

CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(255) PRIMARY KEY,
    token VARCHAR(255),
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    bio VARCHAR(255),
    image VARCHAR(255)
    );

DROP TABLE users;