ALTER TABLE webhook_delivery
    ADD COLUMN lease_until TIMESTAMP NULL,
    ADD COLUMN attempts INT NOT NULL DEFAULT 0,
    ADD COLUMN last_error VARCHAR(2000) NULL;

CREATE INDEX idx_webhook_delivery_status_lease
    ON webhook_delivery(status, lease_until);