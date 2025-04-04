
USE geekstack

CREATE TABLE fcm_tokens (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),  -- Generates UUID as a string
    user_id CHAR(36) NOT NULL,                 -- Store UUID as a string
    token VARCHAR(255) NOT NULL UNIQUE,                -- Unique FCM token per device
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(userId) ON DELETE CASCADE
);
