
CREATE DATABASE IF NOT EXISTS payment_gateway;

USE payment_gateway;


CREATE TABLE IF NOT EXISTS Accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00
        CHECK (balance >= 0),

    per_transaction_limit DECIMAL(15, 2) NOT NULL DEFAULT 5000.00,

    daily_limit DECIMAL(15, 2) NOT NULL DEFAULT 10000.00,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);



CREATE TABLE IF NOT EXISTS Merchants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(255) NOT NULL,

    webhook_url VARCHAR(2048) NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);




CREATE TABLE IF NOT EXISTS Transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    status ENUM(
        'PENDING',
        'PROCESSING',
        'APPROVED',
        'DECLINED',
        'REFUNDED'
    ) NOT NULL,

    account_id BIGINT NOT NULL,

    merchant_id BIGINT NOT NULL,

    amount DECIMAL(15, 2) NOT NULL
        CHECK (amount > 0),

    decline_reason ENUM(
        'INSUFFICIENT_FUNDS',
        'PER_TRANSACTION_LIMIT_EXCEEDED',
        'DAILY_LIMIT_EXCEEDED'
    ) NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_transaction_account
        FOREIGN KEY (account_id)
        REFERENCES Accounts(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_transaction_merchant
        FOREIGN KEY (merchant_id)
        REFERENCES Merchants(id)
        ON DELETE CASCADE
);



CREATE INDEX idx_tx_account_status_created
    ON Transactions (account_id, status, created_at);



CREATE INDEX idx_tx_merchant_created
    ON Transactions (merchant_id, created_at);




CREATE TABLE IF NOT EXISTS Idempotency_Keys (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    idempotency_key VARCHAR(255) NOT NULL,

    request_hash VARCHAR(64) NOT NULL,

    account_id BIGINT NOT NULL,

    transaction_id BIGINT NULL,

    status ENUM(
        'PROCESSING',
        'COMPLETED',
        'FAILED'
    ) NOT NULL DEFAULT 'PROCESSING',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_idempotency_account_key
        UNIQUE (account_id, idempotency_key),

    CONSTRAINT fk_idempotency_account
        FOREIGN KEY (account_id)
        REFERENCES Accounts(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_idempotency_transaction
        FOREIGN KEY (transaction_id)
        REFERENCES Transactions(id)
        ON DELETE SET NULL
);


CREATE INDEX idx_idempotency_transaction
    ON Idempotency_Keys (transaction_id);




CREATE TABLE IF NOT EXISTS Outbox_Events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    event_id CHAR(36) NOT NULL,

    event_type VARCHAR(100) NOT NULL,

    aggregate_type VARCHAR(100) NOT NULL,

    aggregate_id BIGINT NOT NULL,

    destination_url VARCHAR(2048) NOT NULL,

    payload JSON NOT NULL,

    status ENUM(
        'PENDING',
        'PROCESSING',
        'SENT',
        'FAILED'
    ) NOT NULL DEFAULT 'PENDING',

    attempts INT NOT NULL DEFAULT 0,

    next_attempt_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    processing_started_at TIMESTAMP NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    sent_at TIMESTAMP NULL,

    last_error TEXT NULL,

    CONSTRAINT uk_outbox_event_id
        UNIQUE (event_id)
);



CREATE INDEX idx_outbox_status_next_attempt
    ON Outbox_Events (status, next_attempt_at, created_at);



CREATE INDEX idx_outbox_processing_started
    ON Outbox_Events (status, processing_started_at);



CREATE INDEX idx_outbox_aggregate
    ON Outbox_Events (aggregate_type, aggregate_id);

ALTER TABLE idempotency_keys
DROP INDEX uk_idempotency_key;

ALTER TABLE idempotency_keys
    ADD CONSTRAINT uk_account_idempotency_key
        UNIQUE (account_id, idempotency_key);


