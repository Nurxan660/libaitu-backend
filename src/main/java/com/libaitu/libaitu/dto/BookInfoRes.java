package com.libaitu.libaitu.dto;


import com.libaitu.libaitu.entity.BookCategory;
import com.libaitu.libaitu.entity.Categories;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookInfoRes {

    private String bookName;
    private String bookDescription;
    private String bookImageUrl;
    private String bookAuthor;
    private String yearOfPublishing;
    private Integer amountOfBooks;
    private Integer rating;
    private List<String> category;
    private boolean isRecommended;
    private boolean isNew;


}
