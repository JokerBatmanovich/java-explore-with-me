package ru.prackticum.user.service;

import ru.prackticum.user.model.User;

import java.util.List;

public interface UserService {
    User getById(Long id);

    List<User> get(Long[] ids, Long from, Integer size);

    User save(User user);

    void delete(Long id);
}
