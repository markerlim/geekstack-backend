DROP DATABASE IF EXISTS geekstack;
CREATE DATABASE geekstack;

USE geekstack;

CREATE TABLE users (
    userId VARCHAR(28) PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    displaypic VARCHAR(512) DEFAULT 'https://geekstack.dev/icons/geekstackicon.svg',
    email VARCHAR(128) NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
