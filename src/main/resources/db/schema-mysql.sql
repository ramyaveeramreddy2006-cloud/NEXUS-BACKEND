CREATE TABLE IF NOT EXISTS user_domains (
    user_id BIGINT NOT NULL,
    `domain` VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, `domain`)
) ENGINE=InnoDB;
