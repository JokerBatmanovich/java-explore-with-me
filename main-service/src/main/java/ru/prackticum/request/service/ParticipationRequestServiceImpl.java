package ru.prackticum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.prackticum.exception.RequestNotFoundException;
import ru.prackticum.request.dto.ParticipationRequestDto;
import ru.prackticum.request.repository.ParticipationRequestRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService{
    private final ParticipationRequestRepository requestRepository;

    @Override
    public ParticipationRequestDto save(ParticipationRequestDto newRequest) {
        return requestRepository.save(newRequest);
    }

    @Override
    public List<ParticipationRequestDto> saveAll(List<ParticipationRequestDto> newRequests) {
        return requestRepository.saveAll(newRequests);
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        return requestRepository.findAllByRequester(userId);
    }

    @Override
    public ParticipationRequestDto getById(Long requestId) {
        try {
            ParticipationRequestDto request = requestRepository.getReferenceById(requestId);
            System.out.println(request);
            return request;
        } catch (EntityNotFoundException e) {
            throw new RequestNotFoundException(requestId);
        }
    }

    @Override
    public ParticipationRequestDto getByUserAndEvent(Long userId, Long eventId) {
        return requestRepository.findFirstByRequesterAndEvent(userId, eventId);
    }

    @Override
    public List<ParticipationRequestDto> getByEvent(Long eventId) {
        return requestRepository.findAllByEvent(eventId);
    }


    @Override
    public List<ParticipationRequestDto> getByIds(Long[] ids) {
        return requestRepository.findAllByIdIn(Arrays.asList(ids));
    }
}
