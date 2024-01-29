CREATE TABLE IF NOT EXISTS pquests_quests (
    `quest_id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(64) NOT NULL,
    `type` VARCHAR(64) NOT NULL,
    `icon` VARCHAR(64) NOT NULL,
    `description` TEXT(1000) NULL,
    `goal` INT NOT NULL,
    `data` JSON NOT NULL,
    PRIMARY KEY (`quest_id`)
);

CREATE TABLE IF NOT EXISTS pquests_rewards (
    `reward_id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(64) NOT NULL,
    `command` VARCHAR(128) NOT NULL,
    `quest_id` INT NOT NULL,
    PRIMARY KEY (`reward_id`),
    FOREIGN KEY (`quest_id`) REFERENCES pquests_quests (`quest_id`)
);

CREATE TABLE IF NOT EXISTS pquests_players (
    `player_uuid` VARCHAR(36) NOT NULL,
    `player_name` VARCHAR(16) NOT NULL,
    PRIMARY KEY (`player_uuid`)
);

CREATE TABLE IF NOT EXISTS pquests_players_quests_progress (
    `quest_id` INT NOT NULL,
    `player_uuid` VARCHAR(36) NOT NULL,
    `active` TINYINT NOT NULL,
    `progress` INT NOT NULL,
    `completed` TINYINT(1) NULL,
    PRIMARY KEY (`quest_id`, `player_uuid`),
    FOREIGN KEY (`quest_id`) REFERENCES pquests_quests (`quest_id`),
    FOREIGN KEY (`player_uuid`) REFERENCES pquests_players (`player_uuid`)
);