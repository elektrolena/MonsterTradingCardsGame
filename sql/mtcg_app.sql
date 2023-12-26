CREATE DATABASE mtcgdb;
/*DROP DATABASE mtcgdb;*/

\c mtcgdb;

CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
    );

/*DROP TABLE users;*/