package ru.prackticum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.prackticum.catergory.model.Location;
import ru.prackticum.event.validation.ValidLocation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotNull
    @NotBlank(message = "Заголовок не может быть пустым")
    @Length(min = 3, max = 120)
    private String title;
    @NotNull
    @NotBlank(message = "Аннотация не может быть пустой")
    @Length(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private Long category;
    @NotNull(message = "Описание не может быть пустым")
    @NotBlank(message = "Описание не может быть пустым")
    @Length(min = 20, max = 7000)
    private String description;
    @NotNull
    private String eventDate;
    @NotNull
    @ValidLocation
    private Location location;
    @Builder.Default
    private Boolean paid = false;
    @Min(0)
    @Builder.Default
    private Integer participantLimit = 0;
    @Builder.Default
    private Boolean requestModeration = true;

}
