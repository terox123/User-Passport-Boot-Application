CREATE TABLE java_users
(
    id            INT AUTO_INCREMENT NOT NULL,
    username      VARCHAR(255)       NULL,
    age           INT                NULL,
    email         VARCHAR(255)       NULL,
    created_at    datetime           NULL,
    date_of_birth date               NULL,
    gender        VARCHAR(100)       NULL,
    password      VARCHAR(255)       NULL,
    `role`        VARCHAR(100)       NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE passport
(
    id               INT AUTO_INCREMENT NOT NULL,
    serial           VARCHAR(4)         NOT NULL,
    number           VARCHAR(6)         NOT NULL,
    user_id          INT                NOT NULL,
    controlDigit     INT                NOT NULL,
    dateOfReceipt    date               NOT NULL,
    expirationDate   date               NOT NULL,
    statusOfPassport VARCHAR(255)       NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

ALTER TABLE passport
    ADD CONSTRAINT passport_java_users_id_fk FOREIGN KEY (user_id) REFERENCES java_users (id) ON DELETE NO ACTION;

CREATE INDEX passport_java_users_id_fk ON passport (user_id);