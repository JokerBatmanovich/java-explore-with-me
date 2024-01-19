package ru.prackticum.user.dto;

import org.springframework.stereotype.Component;
import ru.prackticum.user.model.User;

@Component
public class UserMapper {
    public User toEntity(NewUserDto newUser) {
        return User.builder()
                .email(newUser.getEmail())
                .name(newUser.getName())
                .build();
    }
}
