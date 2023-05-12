package com.libaitu.libaitu.repository;


import com.libaitu.libaitu.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
