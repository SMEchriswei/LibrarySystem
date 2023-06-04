package com.ascending.training.repository;

import com.ascending.training.model.Role;

public interface RoleDao {
    Role getRoleByName(String name);
}