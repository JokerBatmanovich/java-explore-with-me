package ru.prackticum.compilation.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.prackticum.compilation.model.Compilation;
import ru.prackticum.event.dto.EventMapper;
import ru.prackticum.event.model.Event;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CompilationMapper {
    private final EventMapper eventMapper;

    public Compilation toEntity(NewCompilationDto compilationDto, List<Event> events) {
        return Compilation.builder()
                .title(compilationDto.getTitle())
                .pinned(compilationDto.getPinned())
                .events(events)
                .build();
    }

    public CompilationDto toDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(eventMapper.toShortDtoList(compilation.getEvents()))
                .build();
    }

    public List<CompilationDto> toDtoList(List<Compilation> compilations) {
        return compilations.stream().map(this::toDto).collect(Collectors.toList());
    }
}
