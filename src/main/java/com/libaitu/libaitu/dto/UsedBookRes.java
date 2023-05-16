package com.libaitu.libaitu.dto;

import com.libaitu.libaitu.entity.EBookingStatuses;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsedBookRes {

    private Integer bookingId;
    private String bookName;
    private String bookImageUrl;
    private EBookingStatuses bookingStatus;
    private long remainForReturn;


}
