package ru.prackticum;

import lombok.RequiredArgsConstructor;
import org.springframework.web.util.UriUtils;
import ru.prackticum.model.Hit;
import org.springframework.stereotype.Component;
import ru.prackticum.dto.HitToGetDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class HitMapper {

    public Hit toEntity (HitToGetDto hitToGet, Long id) {
        return Hit.builder()
                .id(id)
                .app(hitToGet.getApp())
                .ip(hitToGet.getIp())
                .uri(hitToGet.getUri())
                .timestamp(LocalDateTime.parse(UriUtils.decode(hitToGet.getTimestamp(), StandardCharsets.UTF_8), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    public Hit toEntity (HitToGetDto hitToGet) {
        return Hit.builder()
                .app(hitToGet.getApp())
                .ip(hitToGet.getIp())
                .uri(hitToGet.getUri())
                .timestamp(LocalDateTime.parse(UriUtils.decode(hitToGet.getTimestamp(), StandardCharsets.UTF_8), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
