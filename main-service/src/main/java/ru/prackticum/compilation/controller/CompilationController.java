package ru.prackticum.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.prackticum.compilation.dto.CompilationDto;
import ru.prackticum.compilation.dto.CompilationMapper;
import ru.prackticum.compilation.dto.NewCompilationDto;
import ru.prackticum.compilation.dto.UpdateCompilationDto;
import ru.prackticum.compilation.model.Compilation;
import ru.prackticum.compilation.service.CompilationService;
import ru.prackticum.event.model.Event;
import ru.prackticum.event.service.EventService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@RestController
@RequestMapping
@Validated
@RequiredArgsConstructor
public class CompilationController {
    @Qualifier("compilationServiceImpl")
    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;
    @Qualifier("eventServiceImpl")
    private final EventService eventService;

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        List<Event> events = eventService.getByIds(newCompilationDto.getEvents());
        Compilation compilation = compilationMapper.toEntity(newCompilationDto, events);
        return compilationMapper.toDto(compilationService.save(compilation));
    }

    @DeleteMapping ("/admin/compilations/{compilationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteCompilation(@PathVariable Long compilationId) {
        return compilationService.delete(compilationId);
    }

    @GetMapping("/compilations/{compilationId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getById(@PathVariable Long compilationId) {
        return compilationMapper.toDto(compilationService.getById(compilationId));
    }

    @GetMapping("/compilations")
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getByParams(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                      @RequestParam(name = "from", defaultValue = "0") Integer from,
                                      @RequestParam(name = "size", defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(from/size, size);
        return compilationMapper.toDtoList(compilationService.getByParams(pinned, pageable));
    }

    @PatchMapping("/admin/compilations/{compilationId}")
    @ResponseStatus(HttpStatus.OK)
    public Compilation updateCompilation(@PathVariable Long compilationId,
                                         @Valid @RequestBody UpdateCompilationDto compilationDto) {

        Compilation compilation = compilationService.getById(compilationId);
        if (compilationDto.getPinned() != null) {
            compilation.setPinned(compilationDto.getPinned());
        }
        if (compilationDto.getTitle() != null ) {
            if (compilationDto.getTitle().isBlank()) {
                throw new ValidationException("Поле title не должно быть пустым");
            }
            compilation.setTitle(compilation.getTitle());
        }
        if (compilationDto.getEvents() != null) {
            compilation.setEvents(eventService.getByIds(compilationDto.getEvents()));
        }
        return compilationService.save(compilation);
    }
}
