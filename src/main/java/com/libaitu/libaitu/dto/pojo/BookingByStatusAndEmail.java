package com.libaitu.libaitu.dto.pojo;

import com.libaitu.libaitu.entity.EBookingStatuses;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingByStatusAndEmail {

    private Integer bookingId;
    private String username;
    private String fullName;
    private String email;
    private EBookingStatuses bookingStatus;
    private String phoneNumber;
    private String bookName;
    private long remainTime;
}
