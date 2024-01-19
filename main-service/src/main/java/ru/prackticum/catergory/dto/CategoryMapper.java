package ru.prackticum.catergory.dto;

import org.springframework.stereotype.Component;
import ru.prackticum.catergory.model.Category;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    public Category toEntity(NewCategoryDto newCategoryDto, Long id) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .id(id)
                .build();
    }

    public Category toEntity(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public List<CategoryDto> toDtoList(List<Category> categories) {
        return categories.stream().map(this::toDto).collect(Collectors.toList());
    }
}
