package com.edu.ulab.app.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private Long id;
    private String fullName;
    private String title;
    private int age;
}
