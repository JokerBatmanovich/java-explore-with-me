package ru.prackticum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;
import ru.prackticum.dto.HitToGetDto;
import ru.prackticum.dto.ViewStats;
import ru.prackticum.exception.IllegalParamException;
import ru.prackticum.service.StatsService;

import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public String add(@RequestBody HitToGetDto hitToGetDto) {
        statsService.add(hitToGetDto);
        return "Информация сохранена";
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam(name = "start") String startParam,
                                    @RequestParam(name = "end") String endParam,
                                    @RequestParam(name = "uris", required = false, defaultValue = "") String[] uris,
                                    @RequestParam(name = "unique", required = false) String uniqueParam) {
        LocalDateTime start;
        LocalDateTime end;
        boolean unique;
        try {
            start = LocalDateTime.parse(UriUtils.decode(startParam, StandardCharsets.UTF_8), formatter);
        } catch (DateTimeException e) {
            throw new IllegalParamException("start", startParam);
        }
        try {
            end = LocalDateTime.parse(UriUtils.decode(endParam, StandardCharsets.UTF_8), formatter);
        } catch (DateTimeException e) {
            throw new IllegalParamException("end", endParam);
        }
        try {
            unique = Boolean.parseBoolean(uniqueParam);
        } catch (DateTimeException e) {
            throw new IllegalParamException("unique", uniqueParam);
        }

        if (uris.length == 0) {
            return statsService.getStats(start, end, unique);
        } else {
            return statsService.getStatsByUris(uris, start, end, unique);
        }
    }
}
