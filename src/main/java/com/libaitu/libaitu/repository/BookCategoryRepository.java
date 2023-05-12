package com.libaitu.libaitu.repository;

import com.libaitu.libaitu.entity.BooksCategories;
import org.springframework.data.repository.CrudRepository;

public interface BookCategoryRepository extends CrudRepository<BooksCategories, Integer> {
}
