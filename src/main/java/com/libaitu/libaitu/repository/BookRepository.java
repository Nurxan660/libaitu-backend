package com.libaitu.libaitu.repository;

import com.libaitu.libaitu.entity.Books;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Books, Integer> {
    Page<Books> findAll(Pageable pageable);
    @Query(value="select * from books where book_author||' '||book_name ilike %:pattern%",nativeQuery = true)
    Page<Books> findByPattern(@Param("pattern") String pattern, Pageable pageable);
}
