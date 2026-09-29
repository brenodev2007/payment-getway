package com.brenodev.payment_getway.Repositories;

import com.brenodev.payment_getway.Entity.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdempotencyRepository extends JpaRepository<IdempotencyKey, Long> {

    Optional<IdempotencyKey> findByKey(String key);
}
