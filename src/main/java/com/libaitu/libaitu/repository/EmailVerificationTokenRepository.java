package com.libaitu.libaitu.repository;

import com.libaitu.libaitu.entity.EmailVerificationToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends CrudRepository<EmailVerificationToken,Long> {
    Optional<EmailVerificationToken> findByToken(String token);
}
