package com.edu.ulab.app.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Book {

    private Long id;
    private Long userId;
    private String title;
    private String author;
    private long pageCount;
}
