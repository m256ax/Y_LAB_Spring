package com.edu.ulab.app.storage.impl;

import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.storage.UserStorage;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserStorageImpl implements UserStorage {

    private final Map<Long, User> userMap = new HashMap<>();

    @Override
    public void update(User user) {
        userMap.replace(user.getId(), user);
    }

    @Override
    public Optional<User> findById(Long userId)  {
        return Optional.ofNullable(userMap.get(userId));
    }

    @Override
    public void delete(Long userId) {
        userMap.remove(userId);
    }

    @Override
    public void save(User user) {
        userMap.put(user.getId(), user);
    }


    @Override
    public boolean containUser (User user) {
        return userMap.containsValue(user);
    }
}
