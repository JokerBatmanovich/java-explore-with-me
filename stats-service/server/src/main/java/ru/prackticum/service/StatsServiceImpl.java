package ru.prackticum.service;

import lombok.RequiredArgsConstructor;
import ru.prackticum.HitMapper;
import ru.prackticum.dto.ViewStats;
import ru.prackticum.model.Hit;
import ru.prackticum.repository.HitRepository;
import org.springframework.stereotype.Service;
import ru.prackticum.dto.HitToGetDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    final HitRepository hitRepository;
    final HitMapper hitMapper;

    @Override
    public void add(HitToGetDto hitToGetDto) {
        Hit savedHit = hitRepository.save(hitMapper.toEntity(hitToGetDto));
        System.out.println(savedHit);
    }

    @Override
    public List<ViewStats> getStatsByUris(String[] uris, LocalDateTime start, LocalDateTime end, boolean unique) {

        if (unique){
            return hitRepository.getStatsByUrisWithUniqueIp(uris, start, end);
        } else {
            return hitRepository.getStatsByUris(uris, start, end);
        }
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, boolean unique) {
        if (unique){
            return hitRepository.getStatsWithoutUris(start, end);
        } else {
            return hitRepository.getStatsWithoutUrisWithUniqueIp(start, end);
        }
    }
}
