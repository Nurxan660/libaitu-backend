package com.libaitu.libaitu.repository;

import com.libaitu.libaitu.entity.Books;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Books, Integer> {
    Page<Books> findAll(Pageable pageable);
}
