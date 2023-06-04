
package com.ascending.training.repository;

import com.ascending.training.model.User;

public interface UserDao {
    boolean save(User user);
    User getUserByEmail(String email);
    User getUserByCredentials(String email, String password);
}