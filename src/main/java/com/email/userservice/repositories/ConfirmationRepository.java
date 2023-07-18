package com.email.userservice.repositories;

import com.email.userservice.domain.Confirmation;
import com.email.userservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {
    Confirmation findByToken(String token);
}