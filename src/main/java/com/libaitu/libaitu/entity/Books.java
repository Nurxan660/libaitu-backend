package com.libaitu.libaitu.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="books")
@Getter
@Setter
@NoArgsConstructor
public class Books {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer bookId;
    @Column(unique = true)
    private String bookName;
    @Column(columnDefinition = "text")
    private String bookDescription;
    private String bookImageUrl;
    private String bookAuthor;
    private String yearOfPublishing;
    private Integer amountOfBooks;
    private Integer rating;
    private boolean isRecommended;
    @OneToMany(mappedBy = "books", cascade = CascadeType.ALL)
    private List<BookCategory> bookCategories;
















}
