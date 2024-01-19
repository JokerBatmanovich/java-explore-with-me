package ru.prackticum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.prackticum.dto.HitToGetDto;
import ru.prackticum.exception.IllegalParamException;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@Validated
@RequestMapping(path= "")
@Slf4j
@RequiredArgsConstructor
public class StatsController {

    private final StatsClient statsClient;

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam(name = "start")
                                               @DateTimeFormat(pattern = "yyy-MM-dd HH-mm-ss")
                                               @JsonFormat(pattern = "yyy-MM-dd HH-mm-ss") LocalDateTime startParam,
                                    @RequestParam(name = "end")
                                        @DateTimeFormat(pattern = "yyy-MM-dd HH-mm-ss")
                                        @JsonFormat(pattern = "yyy-MM-dd HH-mm-ss") LocalDateTime endParam,
                                    @RequestParam(name = "uris", defaultValue = "") String[] uris,
                                    @RequestParam(name = "unique", required = false) String uniqueParam) {
        log.info("Get stats: start={}, end={}, uris={}, unique={}", startParam, endParam, uris, uniqueParam);

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
