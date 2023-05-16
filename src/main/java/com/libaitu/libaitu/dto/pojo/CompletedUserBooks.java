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
public class CompletedUserBooks {

    private Integer bookingId;
    private String bookImageUrl;
    private EBookingStatuses bookingStatus;
    private String bookName;
    private long returned;
}
