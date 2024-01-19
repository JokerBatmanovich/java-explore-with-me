package ru.prackticum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationDto {
    @UniqueElements(message = "Элементы списка не должны повторяться")
    private List<Long> events = new ArrayList<>();
    private Boolean pinned;
    @Length(min = 1, max = 50)
    private String title;
}
