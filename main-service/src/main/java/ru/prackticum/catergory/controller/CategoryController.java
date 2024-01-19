package ru.prackticum.catergory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.prackticum.StatsClient;
import ru.prackticum.catergory.dto.CategoryDto;
import ru.prackticum.catergory.dto.CategoryMapper;
import ru.prackticum.catergory.dto.NewCategoryDto;
import ru.prackticum.catergory.model.Category;
import ru.prackticum.catergory.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping
@Validated
@RequiredArgsConstructor
public class CategoryController {
    @Qualifier("categoryServiceImpl")
    final CategoryService categoryService;
    final StatsClient statsClient;
    final CategoryMapper categoryMapper;

    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getAll(@RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                 @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {

        return categoryMapper.toDtoList(categoryService.getAll(from, size));
    }

    @GetMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getById(@PathVariable Long catId) {
        return categoryMapper.toDto(categoryService.getById(catId));
    }

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto newCategory) {
        Category category = categoryMapper.toEntity(newCategory);
        return categoryMapper.toDto(categoryService.save(category));
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteCategory(@PathVariable Long catId) {
        categoryService.delete(catId);
        return "Информация удалена.";
    }

    @PatchMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public Category updateCategory(@PathVariable Long catId, @Valid @RequestBody NewCategoryDto newCategory) {
        Category category = categoryMapper.toEntity(newCategory, catId);
        return categoryService.save(category);
    }
}
