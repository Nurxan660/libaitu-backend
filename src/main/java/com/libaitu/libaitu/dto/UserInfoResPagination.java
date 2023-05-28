package com.libaitu.libaitu.dto;

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
public class UserInfoResPagination {

    private List<UserInfoRes> content;
    private int totalPages;
}
