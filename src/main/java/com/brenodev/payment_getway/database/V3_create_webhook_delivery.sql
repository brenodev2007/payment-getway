CREATE TABLE webhook_delivery (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id CHAR(36) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    delivered_at TIMESTAMP NULL,
    CONSTRAINT uk_webhook_delivery_event_id UNIQUE (event_id)
);