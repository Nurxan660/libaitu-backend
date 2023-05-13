package com.libaitu.libaitu.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoBookingRequest {

    private Integer amountOfDay;
    private Integer bookId;
    private String phoneNumber;
}
