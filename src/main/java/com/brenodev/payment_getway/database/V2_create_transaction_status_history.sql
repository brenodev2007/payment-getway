CREATE TABLE transaction_status_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_id BIGINT NOT NULL,
    from_status VARCHAR(50),
    to_status VARCHAR(50) NOT NULL,
    reason VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_history_transaction,
    FOREIGN KEY (transaction_id)
    REFERENCES Transactions(id)
);]



CREATE INDEX idx_history_transaction_created
    ON transaction_status_history(transaction_id, created_at);