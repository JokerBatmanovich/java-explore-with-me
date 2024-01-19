package ru.prackticum.catergory.service;

import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.prackticum.catergory.model.Category;
import ru.prackticum.catergory.repository.JpaCategoryRepository;
import ru.prackticum.exception.CategoryNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    final JpaCategoryRepository categoryRepository;

    @Override
    public Category save(Category newCategory) {
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category getById(Long id) {
        try {
            Category category = categoryRepository.getReferenceById(id);
            System.out.println(category);
            return category;
        } catch (EntityNotFoundException e) {
            throw new CategoryNotFoundException(id);
        }
    }

    @Override
    public List<Category> getAll(Integer from, Integer size) {
        return categoryRepository.getAll(from, size);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
