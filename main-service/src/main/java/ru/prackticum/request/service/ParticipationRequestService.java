package ru.prackticum.request.service;

import ru.prackticum.request.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {
    ParticipationRequestDto save(ParticipationRequestDto newRequest);

    List<ParticipationRequestDto> saveAll(List<ParticipationRequestDto> newRequest);

    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto getById(Long requestId);

    ParticipationRequestDto getByUserAndEvent(Long userId, Long eventId);

    List<ParticipationRequestDto> getByEvent(Long eventId);

    List<ParticipationRequestDto> getByIds(Long[] ids);

}
