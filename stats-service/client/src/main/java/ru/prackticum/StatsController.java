package ru.prackticum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;
import ru.prackticum.dto.HitToGetDto;
import ru.prackticum.exception.IllegalParamException;

import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@Validated
@RequestMapping(path= "")
@Slf4j
@RequiredArgsConstructor
public class StatsController {
    private final StatsClient statsClient;
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam(name = "start") String startParam,
                                    @RequestParam(name = "end") String endParam,
                                    @RequestParam(name = "uris", required = false, defaultValue = "") String[] uris,
                                    @RequestParam(name = "unique", required = false) String uniqueParam) {
        log.info("Get stats: start={}, end={}, uris={}, unique={}", startParam, endParam, uris, uniqueParam);
        try {
            LocalDateTime.parse(UriUtils.decode(startParam, StandardCharsets.UTF_8), formatter);
        } catch (DateTimeException e) {
            throw new IllegalParamException("start", startParam);
        }
        try {
            LocalDateTime.parse(UriUtils.decode(endParam, StandardCharsets.UTF_8), formatter);
        } catch (DateTimeException e) {
            throw new IllegalParamException("end", endParam);
        }
        try {
            Boolean.parseBoolean(uniqueParam);
        } catch (DateTimeException e) {
            throw new IllegalParamException("unique", uniqueParam);
        }
        Map<String, Object> parameters = Map.of(
                "start", startParam,
                "end", endParam,
                "uris", uris,
                "unique", uniqueParam
        );
        return statsClient.getStats(parameters);
    }

    @PostMapping
    public ResponseEntity<Object> add(HitToGetDto hitToGetDto) {
        return statsClient.add(hitToGetDto);
    }
}
