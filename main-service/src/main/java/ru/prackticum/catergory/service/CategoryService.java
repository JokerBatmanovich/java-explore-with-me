package ru.prackticum.catergory.service;

import ru.prackticum.catergory.model.Category;

import java.util.List;

public interface CategoryService {

    Category save(Category newCategory);

    Category getById(Long id);

    List<Category> getAll(Integer from, Integer size);

    void delete(Long id);
}
