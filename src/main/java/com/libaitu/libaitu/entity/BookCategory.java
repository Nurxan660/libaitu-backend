package com.libaitu.libaitu.entity;

import com.libaitu.libaitu.compositeKey.BookCategoryKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BookCategory {
    @EmbeddedId
    private BookCategoryKey bookCategoryKey;

    @ManyToOne
    @JoinColumn(name="book_id")
    @MapsId("bookId")
    private Books books;

    @ManyToOne()
    @JoinColumn(name="category_id")
    @MapsId("categoryId")
    private Categories categories;










}
