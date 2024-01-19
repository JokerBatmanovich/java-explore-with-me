package ru.prackticum.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.prackticum.event.enums.State;
import ru.prackticum.event.model.Event;
import ru.prackticum.event.service.EventService;
import ru.prackticum.exception.ConflictException;
import ru.prackticum.request.dto.EventRequestStatusUpdateRequest;
import ru.prackticum.request.dto.EventRequestStatusUpdateResult;
import ru.prackticum.request.dto.ParticipationRequestDto;
import ru.prackticum.request.service.ParticipationRequestService;
import ru.prackticum.request.util.Status;
import ru.prackticum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@Validated
@RequiredArgsConstructor
@Transactional
public class ParticipationRequestController {
    @Qualifier("participationRequestServiceImpl")
    private final ParticipationRequestService requestService;
    @Qualifier("eventServiceImpl")
    private final EventService eventService;
    @Qualifier("userServiceImpl")
    private final UserService userService;

    @GetMapping("users/{userId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId){

        userService.getById(userId);
        return requestService.getUserRequests(userId);
    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequest(@PathVariable Long userId,
                                              @RequestParam (name = "eventId") Long eventId) {

        userService.getById(userId);
        Event event = eventService.getById(eventId);

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Нельзя отправить запрос на свое же событие.");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Нельзя отправить запрос на неопубликованное событие.");
        }
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new ConflictException("Достигнут лимит заявок.");
        }
        if (requestService.getByUserAndEvent(userId, eventId) != null) {
            throw new ConflictException("Нельзя отправить повторный запрос.");
        }
        ParticipationRequestDto request = ParticipationRequestDto.builder()
                .requester(userId)
                .event(eventId)
                .created(LocalDateTime.now())
                .build();
        if (event.getParticipantLimit() == 0) {
            request.setStatus(Status.CONFIRMED);
            event.increaseRequests();
            eventService.save(event);
        } else {
            if (!event.getRequestModeration()) {
                if (event.getParticipantLimit() - event.getConfirmedRequests() > 0) {
                    request.setStatus(Status.CONFIRMED);
                    event.increaseRequests();
                    eventService.save(event);
                } else {
                    request.setStatus(Status.REJECTED);
                }
            } else {
                request.setStatus(Status.PENDING);
            }
        }

        return requestService.save(request);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId,
                                                 @PathVariable Long requestId) {

        userService.getById(userId);
        ParticipationRequestDto request = requestService.getById(requestId);

        if (!request.getRequester().equals(userId)) {
            throw new ConflictException("У пользователя нет парв на отмену чужого запроса.");
        }
        if (request.getStatus().equals(Status.CONFIRMED)) {
            throw new ConflictException("Нельзя отменить принятый запрос.");
        }
        request.setStatus(Status.CANCELED);
        return requestService.save(request);
    }

    @GetMapping("users/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId,
                                                         @PathVariable Long eventId){

        userService.getById(userId);
        Event event = eventService.getById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("У пользователя нет доступа к запросам на участие чужого события.");
        }
        return requestService.getByEvent(eventId);
    }


    @PatchMapping("users/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult changeRequestsStatus(@PathVariable Long userId,
                                                        @PathVariable Long eventId,
                                                        @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        Status status = Status.valueOf(updateRequest.getStatus());
        if (!status.equals(Status.REJECTED) && !status.equals(Status.CONFIRMED)) {
            throw new ConflictException("Статус может принимать только значения REJECTED и CONFIRMED");
        }

        userService.getById(userId);
        Event event = eventService.getById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("У пользователя нет доступа к запросам на участие чужого события.");
        }

        Long[] idsToUpdate = updateRequest.getRequestIds();
        int participationLimit = event.getParticipantLimit();
        int confirmedRequests = event.getConfirmedRequests();
        int actualCapacity = participationLimit - confirmedRequests;
        if (actualCapacity == 0 && status.equals(Status.CONFIRMED)) {
            throw new ConflictException("Достигнут лимит заявок");
        }
        List<ParticipationRequestDto> requestsToUpdate = requestService.getByIds(idsToUpdate);

        for (ParticipationRequestDto request : requestsToUpdate) {
            if (request.getStatus() != Status.PENDING) {
                throw new ConflictException(String.format("Нельзя изменить статус запроса (ID=%d), " +
                        "который равен %s", request.getId(), request.getStatus().toString()));
            }
            if (actualCapacity > 0 && status.equals(Status.CONFIRMED)) {
                request.setStatus(Status.CONFIRMED);
                event.increaseRequests();
                actualCapacity--;
            } else {
                request.setStatus(Status.REJECTED);
            }
        }
        eventService.save(event);
        requestService.saveAll(requestsToUpdate);
        return mapToUpdateResult(requestsToUpdate);
    }

    private EventRequestStatusUpdateResult mapToUpdateResult(List<ParticipationRequestDto> requests) {
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        for (ParticipationRequestDto request : requests) {
            if (request.getStatus().equals(Status.CONFIRMED)) {
                result.addConfirmedRequest(request);
            } else {
                result.addRejectedRequest(request);
            }
        }
        return result;
    }
}
