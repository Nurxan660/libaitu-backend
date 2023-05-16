package com.libaitu.libaitu.dto;


import com.libaitu.libaitu.dto.pojo.BookingByStatusAndEmail;
import com.libaitu.libaitu.dto.pojo.Books;
import com.libaitu.libaitu.entity.EBookingStatuses;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetBookingsByStatusAndEmailRes {

    private List<BookingByStatusAndEmail> content;
    private int totalPages;
}
