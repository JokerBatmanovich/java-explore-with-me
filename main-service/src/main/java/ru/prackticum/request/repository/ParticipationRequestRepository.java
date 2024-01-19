package ru.prackticum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.prackticum.request.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequestDto, Long> {
    ParticipationRequestDto findFirstByRequesterAndEvent (Long requesterId, Long eventId);

    List<ParticipationRequestDto> findAllByRequester(Long userId);

    List<ParticipationRequestDto> findAllByEvent(Long eventId);

    List<ParticipationRequestDto> findAllByIdIn(List<Long> ids);

}
