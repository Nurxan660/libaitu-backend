package com.libaitu.libaitu.repository;


import com.libaitu.libaitu.entity.ERole;
import com.libaitu.libaitu.entity.Roles;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Roles,Integer> {
    Optional<Roles> findByRole(ERole role);
}
