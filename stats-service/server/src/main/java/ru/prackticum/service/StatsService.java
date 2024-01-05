package ru.prackticum.service;

import ru.prackticum.dto.HitToGetDto;
import ru.prackticum.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void add (HitToGetDto hitToGetDto);

    List<ViewStats> getStatsByUris(String[] uris, LocalDateTime start, LocalDateTime end, boolean unique);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, boolean unique);
}
