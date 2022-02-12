package ru.specialist.spring.service;

import ru.specialist.spring.entity.User;

public interface UserService {

    public User findByUsername(String username);

    void create(String username, String password);
}
