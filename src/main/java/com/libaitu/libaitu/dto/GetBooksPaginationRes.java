package com.libaitu.libaitu.dto;


import com.libaitu.libaitu.dto.pojo.Books;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetBooksPaginationRes {

    private List<Books> content;
    private int totalPages;
}
