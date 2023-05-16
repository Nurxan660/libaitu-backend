package com.libaitu.libaitu.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRes {

    private String email;
    private String fullName;
    private String userName;
    private long totalDayOfReturn;
}
