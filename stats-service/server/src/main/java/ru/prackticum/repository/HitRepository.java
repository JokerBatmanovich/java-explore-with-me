package ru.prackticum.repository;

import ru.prackticum.dto.ViewStats;
import ru.prackticum.model.Hit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit,Long> {

    @Query("select new ru.prackticum.dto.ViewStats(h.app, h.uri, count(h.ip)) " +
            "from Hit h where h.uri in (:uris) and h.timestamp between (:start) and (:end) " +
            "group by h.ip, h.uri, h.app order by count(h.ip) desc")
    List<ViewStats> getStatsByUris (String[] uris, LocalDateTime start, LocalDateTime end);

    @Query("select new ru.prackticum.dto.ViewStats(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit h where h.uri in (:uris) and h.timestamp between (:start) and (:end) " +
            "group by h.ip, h.uri, h.app order by count(h.ip) desc")
    List<ViewStats> getStatsByUrisWithUniqueIp (String[] uris, LocalDateTime start, LocalDateTime end);

    @Query("select new ru.prackticum.dto.ViewStats(h.app, h.uri, count(h.ip)) " +
            "from Hit h where h.timestamp between (:start) and (:end) " +
            "group by h.ip, h.uri, h.app order by count(h.ip) desc")
    List<ViewStats> getStatsWithoutUris (LocalDateTime start, LocalDateTime end);

    @Query("select new ru.prackticum.dto.ViewStats(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit h where h.timestamp between (:start) and (:end) " +
            "group by h.ip, h.uri, h.app order by count(h.ip) desc")
    List<ViewStats> getStatsWithoutUrisWithUniqueIp (LocalDateTime start, LocalDateTime end);

    @Query("select count(distinct h.ip) from Hit h where h.uri like :uri and h.app like :app")
    int getViews(String uri, String app);
}
