package ru.prackticum.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.prackticum.exception.IncorrectEmailException;
import ru.prackticum.user.dto.NewUserDto;
import ru.prackticum.user.dto.UserMapper;
import ru.prackticum.user.model.User;
import ru.prackticum.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
@Validated
@RequiredArgsConstructor
public class UserController {

    @Qualifier("userServiceImpl")
    final UserService userService;
    final UserMapper userMapper;

    @GetMapping("/admin/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers(@RequestParam(name = "ids", defaultValue = "") Long[] ids,
                               @RequestParam(name = "from", defaultValue = "0") Long from,
                               @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return userService.get(ids, from, size);
    }

    @PostMapping("/admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User add(@Valid @RequestBody NewUserDto newUserDto) {
        String email = newUserDto.getEmail();
        int startIndex = email.indexOf("@");
        String[] domains = email.substring(startIndex + 1).split("\\.");
        for (String domain :domains) {
            if (domain.length() > 64) {
                throw new IncorrectEmailException();
            }
        }

        User user = userMapper.toEntity(newUserDto);
        return userService.save(user);
    }

    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        userService.delete(userId);
    }
}
