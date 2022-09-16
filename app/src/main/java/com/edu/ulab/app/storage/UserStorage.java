package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    void update(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    void delete(Long id);

    void save(User user);

}
