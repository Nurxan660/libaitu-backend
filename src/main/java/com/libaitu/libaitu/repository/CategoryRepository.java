package com.libaitu.libaitu.repository;

import com.libaitu.libaitu.entity.BookCategory;
import com.libaitu.libaitu.entity.Categories;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Categories, Integer> {
    Optional<Categories> findByBookCategoryName(String bookCategoryName);

}
