package ru.prackticum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.prackticum.exception.UserNotFoundException;
import ru.prackticum.user.model.User;
import ru.prackticum.user.repository.JpaUserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    final JpaUserRepository userRepository;

    @Override
    public User getById(Long id) {
        try {
            return userRepository.getReferenceById(id);
        } catch (EntityNotFoundException e) {
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public List<User> get(Long[] ids, Long from, Integer size) {
        if (ids.length == 0) {
            return userRepository.getAll(from, size);
        } else {
            return userRepository.getAllByIdIn(ids);
        }
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
