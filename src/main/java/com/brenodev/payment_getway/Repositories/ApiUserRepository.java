package com.brenodev.payment_getway.Repositories;

import com.brenodev.payment_getway.Entity.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiUserRepository
        extends JpaRepository<ApiUser, Long> {

    Optional<ApiUser> findByUsername(String username);
}
