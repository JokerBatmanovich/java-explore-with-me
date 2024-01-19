package ru.prackticum.compilation.dto;

import lombok.Builder;
import lombok.Data;
import ru.prackticum.event.dto.EventShortDto;

import java.util.List;

@Data
@Builder
public class CompilationDto {
    private Long id;
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;
}
