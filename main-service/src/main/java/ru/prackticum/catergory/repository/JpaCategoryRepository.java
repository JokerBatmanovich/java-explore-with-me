package ru.prackticum.catergory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.prackticum.catergory.model.Category;

import java.util.List;

public interface JpaCategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT * FROM categories ORDER BY id LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Category> getAll(Integer from, Integer size);
}
