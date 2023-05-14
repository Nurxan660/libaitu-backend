package com.libaitu.libaitu.dto;

import com.libaitu.libaitu.entity.EBookingStatuses;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestedBooksRes {
    private Integer bookingId;
    private String bookName;
    private String bookImageUrl;
    private EBookingStatuses bookingStatus;
}
