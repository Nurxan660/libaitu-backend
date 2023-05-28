package com.libaitu.libaitu.repository;


import com.libaitu.libaitu.entity.Books;
import com.libaitu.libaitu.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    @Query(value="select * from users where full_name||' '||email ilike %:pattern%",nativeQuery = true)
    Page<User> findByPattern(@Param("pattern") String pattern, Pageable pageable);
}
