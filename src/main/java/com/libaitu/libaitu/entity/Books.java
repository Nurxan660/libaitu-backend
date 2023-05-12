package com.libaitu.libaitu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="books")
@Getter
@Setter
@NoArgsConstructor
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer bookId;
    private String bookName;
    private String bookDescription;
    private String bookAuthor;
    private String yearOfPublishing;
    @ManyToOne
    @JoinColumn(name = "book_category_id")
    private  BooksCategories booksCategories;











}
