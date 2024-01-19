package ru.prackticum.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.prackticum.user.model.User;

import java.util.List;

public interface JpaUserRepository extends JpaRepository<User, Long> {
    List<User> getAllByIdIn(Long[] ids);

    @Query(value = "SELECT * FROM users ORDER BY id LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<User> getAll(Long from, Integer size);
}
