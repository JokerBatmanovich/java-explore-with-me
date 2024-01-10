package ru.prackticum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;
import ru.prackticum.dto.HitToGetDto;
import ru.prackticum.dto.ViewStats;
import ru.prackticum.exception.IncorrectParameterException;
import ru.prackticum.model.Hit;
import ru.prackticum.service.StatsService;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping
@Validated
@RequiredArgsConstructor()
public class    StatsController {
    @Qualifier("statsServiceImpl")
    final StatsService statsService;
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public Hit add(@RequestBody @Valid HitToGetDto hitToGetDto) {
        return statsService.add(hitToGetDto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStats> getStats(@RequestParam(name = "start") String startParam,
                                    @RequestParam(name = "end") String endParam,
                                    @RequestParam(name = "uris", required = false, defaultValue = "") String[] uris,
                                    @RequestParam(name = "unique", defaultValue = "false") String uniqueParam) {
        LocalDateTime start;
        LocalDateTime end;
        try {
            start = LocalDateTime.parse(UriUtils.decode(startParam, StandardCharsets.UTF_8), formatter);
        } catch (DateTimeParseException e) {
            throw new IncorrectParameterException(String.format("Некорректное значение параметра start: %s",
                    startParam));
        }
        try {
            end = LocalDateTime.parse(UriUtils.decode(endParam, StandardCharsets.UTF_8), formatter);
        } catch (DateTimeParseException e) {
            throw new IncorrectParameterException(String.format("Некорректное значение параметра end: %s",
                    endParam));
        }
        if (start.isAfter(end)) {
            throw new IncorrectParameterException("start не может быть после end");
        }
        if (uniqueParam != null &&
                !(uniqueParam.equalsIgnoreCase("true") || uniqueParam.equalsIgnoreCase("false"))) {
            throw new IncorrectParameterException(
                    String.format("Некорректное значение параметра unique: %s", uniqueParam));
        }
        boolean unique = Boolean.parseBoolean(uniqueParam);

        if (uris.length == 0) {
            return statsService.getStats(start, end, unique);
        } else {
            return statsService.getStatsByUris(uris, start, end, unique);
        }
    }

    @GetMapping("/views")
    public int getViews(@RequestParam(name = "uri") String uri,
                        @RequestParam(name = "app") String app) {
        return statsService.getViews(uri, app);
    }
}
