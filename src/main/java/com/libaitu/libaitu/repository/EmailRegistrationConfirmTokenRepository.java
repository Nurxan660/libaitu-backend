package com.libaitu.libaitu.repository;

import com.libaitu.libaitu.entity.EmailRegistrationConfirmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmailRegistrationConfirmTokenRepository extends JpaRepository<EmailRegistrationConfirmToken, Integer> {
    Optional<EmailRegistrationConfirmToken> findByEmail(String email);
    boolean existsByEmail(String email);
    void deleteByEmail(String email);
}
