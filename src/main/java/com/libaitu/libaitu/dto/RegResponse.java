package com.libaitu.libaitu.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegResponse {


    private String accessToken;
    private String refreshToken;
    private String username;
    private String role;
    private Integer userId;
}
