package com.libaitu.libaitu.compositeKey;


import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
public class BookCategoryKey implements Serializable {
    private Integer bookId;
    private Integer categoryId;
}
