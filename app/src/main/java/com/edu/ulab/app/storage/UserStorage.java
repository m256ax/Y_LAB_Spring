package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.User;

import java.util.Optional;

public interface UserStorage {

    void update(User user);

    Optional<User> findById(Long id);

    void delete(Long id);

    void save(User user);

    boolean containUser(User user);
}
