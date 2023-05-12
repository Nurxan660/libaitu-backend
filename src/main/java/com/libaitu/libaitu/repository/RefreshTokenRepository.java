package com.libaitu.libaitu.repository;

import com.libaitu.libaitu.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken,Integer> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUserUsername(String username);
    Optional<RefreshToken> findByUserUserId(Integer refreshTokenId);
}
