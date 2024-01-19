package ru.prackticum.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.prackticum.dto.HitToGetDto;

@Component
@RequiredArgsConstructor
public class HitMapper {

    public Hit toEntity (HitToGetDto hitToGet) {
        return Hit.builder()
                .app(hitToGet.getApp())
                .ip(hitToGet.getIp())
                .uri(hitToGet.getUri())
                .timestamp(hitToGet.getTimestamp())
                .build();
    }
}
