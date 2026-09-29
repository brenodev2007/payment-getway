
USE payment_gateway;

-- Tabela de contas
CREATE TABLE IF NOT EXISTS Accounts (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00 CHECK (balance >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    per_transaction_limit DECIMAL(15,2) NOT NULL DEFAULT 5000.00,
    daily_limit DECIMAL(15,2) NOT NULL DEFAULT 10000.00
    );

-- Tabela de estabelecimentos
CREATE TABLE IF NOT EXISTS Merchants (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Tabela de transações
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
    amount DECIMAL(15, 2) NOT NULL CHECK (amount > 0),
    decline_reason ENUM(
                           'INSUFFICIENT_FUNDS',
                           'PER_TRANSACTION_LIMIT_EXCEEDED',
                           'DAILY_LIMIT_EXCEEDED'
                       ) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (account_id) REFERENCES Accounts(id)
    ON DELETE CASCADE,

    FOREIGN KEY (merchant_id) REFERENCES Merchants(id)
    ON DELETE CASCADE
    );

-- Índice para consultas de limite diário
CREATE INDEX idx_tx_account_status_created
    ON Transactions (account_id, status, created_at);


-- Tabela de chaves de idempotência
CREATE TABLE IF NOT EXISTS Idempotency_Keys (
                                                id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                                idempotency_key VARCHAR(255) NOT NULL,

    account_id BIGINT NOT NULL,

    transaction_id BIGINT NULL,

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

-- Índice para localizar rapidamente uma chave por conta
CREATE INDEX idx_idempotency_account
    ON Idempotency_Keys (account_id);