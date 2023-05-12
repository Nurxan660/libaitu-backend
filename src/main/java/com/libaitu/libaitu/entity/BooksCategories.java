package com.libaitu.libaitu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BooksCategories {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer bookCategoryId;
    private String bookCategoryName;











}
