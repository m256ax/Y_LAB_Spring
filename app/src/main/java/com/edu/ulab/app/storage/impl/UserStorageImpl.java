package com.edu.ulab.app.storage.impl;

import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.storage.UserStorage;

import java.util.*;

public class UserStorageImpl implements UserStorage {

    private final Map<Long, User> userMap = new HashMap<>();

    @Override
    public void update(User user) {
        userMap.replace(user.getId(), user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.of(userMap.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public void delete(Long id) {
        userMap.remove(id);
    }

    @Override
    public void save(User user) {
        userMap.put(user.getId(), user);
    }


}
