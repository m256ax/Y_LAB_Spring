package com.edu.ulab.app.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private Long id;
    private String fullName;
    private String title;
    private int age;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (age != user.age) return false;
        if (!fullName.equals(user.fullName)) return false;
        return title.equals(user.title);
    }

    @Override
    public int hashCode() {
        int result = fullName.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + age;
        return result;
    }
}
