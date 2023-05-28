package com.libaitu.libaitu.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveBookReq {


    private String bookName;
    private String bookDescription;
    private String bookImageUrl;
    private String bookAuthor;
    private String yearOfPublishing;
    private Integer amountOfBooks;
    private Integer rating;
    private boolean recommended;
    private boolean news;
    private List<String> categories;
}
