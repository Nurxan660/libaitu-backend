package com.libaitu.libaitu.repository;

import com.libaitu.libaitu.compositeKey.BookCategoryKey;
import com.libaitu.libaitu.entity.BookCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategoryKey> {
    Page<BookCategory> findAllByCategoriesBookCategoryNameIn(List<String> bookCategoryNames, Pageable pageable);
    void deleteByBooksBookId(Integer bookId);
    void deleteByBooksBookIdAndCategoriesBookCategoryId(Integer bookId, Integer bookCategoryId);

}
