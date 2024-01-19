package ru.prackticum.service;

import ru.prackticum.dto.HitToGetDto;
import ru.prackticum.dto.ViewStats;
import ru.prackticum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    Hit add (HitToGetDto hitToGetDto);

    List<ViewStats> getStatsByUris(String[] uris, LocalDateTime start, LocalDateTime end, boolean unique);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, boolean unique);

    int getViews(String uri, String app);
}
