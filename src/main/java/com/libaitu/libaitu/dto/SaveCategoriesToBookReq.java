package com.libaitu.libaitu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveCategoriesToBookReq {

    private Integer bookId;
    private List<String> categories;
}
