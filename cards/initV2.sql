DROP DATABASE IF EXISTS geekstack;
CREATE DATABASE geekstack;
USE geekstack;
CREATE TABLE users (
    userId VARCHAR(28) PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    displaypic VARCHAR(512) DEFAULT 'https://geekstack.dev/icons/geekstackicon.svg',
    preferences JSON DEFAULT { },
    email VARCHAR(128) NOT NULL,
    membershipType ENUM('free', 'premium', "vip", 'admin') DEFAULT 'free',
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);