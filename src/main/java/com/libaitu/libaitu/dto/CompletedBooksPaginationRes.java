package com.libaitu.libaitu.dto;

import com.libaitu.libaitu.dto.pojo.BookingByStatusAndEmail;
import com.libaitu.libaitu.dto.pojo.CompletedUserBooks;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompletedBooksPaginationRes {

    private List<CompletedUserBooks> content;
    private int totalPages;

}
