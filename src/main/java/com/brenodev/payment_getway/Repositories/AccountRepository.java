package com.brenodev.payment_getway.Repositories;

import com.brenodev.payment_getway.Entity.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
   Optional<Account> findIdForUpdate(@Param("id")Long id);
}
