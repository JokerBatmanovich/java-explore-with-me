package ru.prackticum.compilation.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    @UniqueElements(message = "Элементы списка не должны повторяться")
    @Builder.Default
    private List<Long> events = new ArrayList<>();
    @Builder.Default
    private Boolean pinned = false;
    @NotNull(message = "Не должно быть пустым")
    @NotBlank(message = "Не должно быть пустым")
    @Length(min = 1, max = 50)
    private String title;
}
