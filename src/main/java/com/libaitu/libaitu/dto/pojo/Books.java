package com.libaitu.libaitu.dto.pojo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Books {

    private Integer bookId;
    private String bookName;
    private String bookAuthor;
}
