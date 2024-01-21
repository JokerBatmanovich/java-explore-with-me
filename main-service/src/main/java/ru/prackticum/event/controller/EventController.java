package ru.prackticum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.prackticum.StatsClient;
import ru.prackticum.catergory.model.Category;
import ru.prackticum.catergory.service.CategoryService;
import ru.prackticum.dto.HitToGetDto;
import ru.prackticum.event.dto.*;
import ru.prackticum.event.enums.State;
import ru.prackticum.event.enums.StateAction;
import ru.prackticum.event.model.Event;
import ru.prackticum.event.service.EventService;
import ru.prackticum.event.utils.EnumController;
import ru.prackticum.event.utils.EventFilter;
import ru.prackticum.exception.*;
import ru.prackticum.user.model.User;
import ru.prackticum.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping
@Validated
@RequiredArgsConstructor
public class EventController {
    @Qualifier("categoryServiceImpl")
    final CategoryService categoryService;
    @Qualifier("eventServiceImpl")
    final EventService eventService;
    @Qualifier("userServiceImpl")
    final UserService userService;
    final EventMapper eventMapper;
    final StatsClient statsClient;
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @Valid @RequestBody NewEventDto newEventDto) {
        LocalDateTime eventDate = LocalDateTime.parse(newEventDto.getEventDate(), formatter);
        if (eventDate.minusHours(2).isBefore(LocalDateTime.now())) {
            throw new IncorrectEventDateException(eventDate);
        }
        Category category = categoryService.getById(newEventDto.getCategory());
        User initiator = userService.getById(userId);

        Event event = eventMapper.toEntity(newEventDto, category, initiator);
        return eventMapper.toFullDto(eventService.save(event));
    }

    @GetMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEvent(HttpServletRequest request, @PathVariable Long eventId) {
        Event event = eventService.getByIdAndState(eventId, State.PUBLISHED);

        if (event != null) {
            sendHit(request);
            event.setViews(getViews(eventId));
            eventService.save(event);
            return eventMapper.toFullDto(event);
        } else {
            throw new EventNotFoundException();
        }
    }

    @PatchMapping("/admin/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventRequest updatedEvent) {
        Category category = null;
        if (updatedEvent.getCategory() != null) {
            category = categoryService.getById(updatedEvent.getCategory());
        }

        Event event = eventService.getById(eventId);
        if (updatedEvent.getStateAction() != null) {
            StateAction stateAction = StateAction.valueOf(updatedEvent.getStateAction());

            if (stateAction != StateAction.REJECT_EVENT && stateAction != StateAction.PUBLISH_EVENT) {
                throw new ForbiddenException("Некорректно составлен запрос: stateAction не может быть равен '"
                        + stateAction + "'. У вас есть права только на значения 'REJECT_EVENT' и 'PUBLISH_EVENT'.");
            }

            if (stateAction == StateAction.PUBLISH_EVENT && event.getState() != State.PENDING) {
                throw new ForbiddenException("Невозможно опубликовать событие, которое находится на стадии: "
                        + event.getState());
            }

            if (stateAction == StateAction.REJECT_EVENT && event.getState() == State.PUBLISHED) {
                throw new ForbiddenException("Невозможно отменить уже опубликованное событие");
            }

            if (stateAction == StateAction.REJECT_EVENT) {
                event.setState(State.CANCELED);
            } else {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }
        }

        if (category != null) {
            event.setCategory(category);
        }
        eventMapper.updateEventFromRequest(updatedEvent, event);
        return eventMapper.toFullDto(eventService.save(event));
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getUserEventById(@PathVariable Long userId,
                                         @PathVariable Long eventId) {
        userService.getById(userId);
        Event event = eventService.getById(eventId);

        return eventMapper.toFullDto(event);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventByUser(@PathVariable Long userId,
                             @PathVariable Long eventId,
                             @Valid @RequestBody UpdateEventRequest updatedEvent) {
        Category category = null;
        if (updatedEvent.getCategory() != null) {
            category = categoryService.getById(updatedEvent.getCategory());
        }

        Event event = eventService.getById(eventId);

        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new NoPermissionsException(String.format("У пользователя с ID=%d нет прав " +
                    "на редактирование события с ID=%d", userId, eventId));
        }

        if (event.getState() == State.PUBLISHED) {
            throw new ForbiddenException("Невозможно изменить уже опубликованное событие");
        }

        if (updatedEvent.getStateAction() != null) {
            StateAction stateAction = StateAction.valueOf(updatedEvent.getStateAction());

            if (stateAction != StateAction.SEND_TO_REVIEW && stateAction != StateAction.CANCEL_REVIEW) {
                throw new ForbiddenException("Некорректно составлен запрос: stateAction не может быть равен '"
                        + stateAction + "'. У вас есть права только на значения 'SEND_TO_REVIEW' и 'CANCEL_REVIEW'.");
            }

            if (stateAction == StateAction.CANCEL_REVIEW) {
                event.setState(State.CANCELED);
            } else {
                event.setState(State.PENDING);
            }
        }

        if (category != null) {
            event.setCategory(category);
        }
        eventMapper.updateEventFromRequest(updatedEvent, event);
        return eventMapper.toFullDto(eventService.save(event));
    }

    @GetMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             @RequestParam(name = "from", defaultValue = "0") Integer from,
                                             @RequestParam(name = "size", defaultValue = "10") Integer size) {
        userService.getById(userId);
        Pageable pageable = PageRequest.of(Math.toIntExact(from/size), size);
        eventService.getUserEvents(userId, pageable);
        return eventMapper.toShortDtoList(eventService.getUserEvents(userId, pageable));
    }

    @GetMapping("/admin/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEventsByAdmin(@RequestParam(name = "users", required = false) Integer[] users,
                                               @RequestParam(name = "states", required = false) String[] states,
                                               @RequestParam(name = "categories", required = false) Integer[] categories,
                                               @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                               @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                               @RequestParam(name = "from", defaultValue = "0") Integer from,
                                               @RequestParam(name = "size", defaultValue = "10") Integer size) {

        EventFilter eventFilter = new EventFilter();

        if (rangeStart != null && !rangeStart.isBlank()) {
            eventFilter.setRangeStart(LocalDateTime.parse(rangeStart, formatter));
        }
        if (rangeEnd != null && !rangeEnd.isBlank()) {
            eventFilter.setRangeEnd(LocalDateTime.parse(rangeEnd, formatter));
        }

        if (eventFilter.getRangeStart() != null && eventFilter.getRangeEnd() != null) {
            if (eventFilter.getRangeStart().isAfter(eventFilter.getRangeEnd())) {
                throw new IncorrectParameterException("rangeStart не может быть позже rangeEnd");
            }
        }

        if (users != null && users.length > 0) {
            eventFilter.setUsers(users);
        }

        if (states != null && states.length > 0) {
            Set<State> statesSet = EnumController.convertToEnumList(State.class, List.of(states));
            if (!statesSet.isEmpty()) {
                eventFilter.setStates(statesSet.toArray(new State[0]));
            }
        }

        if (categories != null && categories.length > 0) {
            eventFilter.setCategories(categories);
        }

        Pageable pageable = PageRequest.of(Math.toIntExact(from/size), size);
        return eventMapper.toFullDtoList(eventService.getEventsByFilter(eventFilter, pageable));
    }

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsByPublic(HttpServletRequest request,
                                                 @RequestParam(name = "text", required = false) String text,
                                                 @RequestParam(name = "categories", required = false) Integer[] categories,
                                                 @RequestParam(name = "paid", required = false) Boolean paid,
                                                 @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                                 @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                                 @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
                                                 @RequestParam(name = "sort", required = false) String sort,
                                                 @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                 @RequestParam(name = "size", defaultValue = "10") Integer size) {
        EventFilter eventFilter = new EventFilter();

        if (rangeStart != null && !rangeStart.isBlank()) {
            eventFilter.setRangeStart(LocalDateTime.parse(rangeStart, formatter));
        }
        if (rangeEnd != null && !rangeEnd.isBlank()) {
            eventFilter.setRangeEnd(LocalDateTime.parse(rangeEnd, formatter));
        }

        if (eventFilter.getRangeStart() != null && eventFilter.getRangeEnd() != null) {
            if (eventFilter.getRangeStart().isAfter(eventFilter.getRangeEnd())) {
                throw new IncorrectParameterException("rangeStart не может быть позже rangeEnd");
            }
        }

        if (text != null) {
            eventFilter.setText(text);
        }

        if (categories != null && categories.length > 0) {
            eventFilter.setCategories(categories);
        }

        if (paid != null) {
            eventFilter.setPaid(paid);
        }

        if (onlyAvailable != null) {
            eventFilter.setOnlyAvailable(onlyAvailable);
        }

        Pageable pageable;
        if (sort != null) {
            if (sort.equalsIgnoreCase("EVENT_DATE")) {
                pageable = PageRequest.of(from, size, Sort.by("eventDate").descending());
            } else if (sort.equalsIgnoreCase("VIEWS")) {
                pageable = PageRequest.of(from, size, Sort.by("views").descending());
            } else {
                pageable = PageRequest.of(from, size, Sort.by("id").ascending());
            }
        } else {
            pageable = PageRequest.of(from, size);
        }

        List<Event> events = eventService.getEventsByFilter(eventFilter, pageable);
        sendHit(request);
        return eventMapper.toShortDtoList(events);
    }

    private void sendHit(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();
        HitToGetDto hit = HitToGetDto.builder()
                .app("ewm-main-service")
                .ip(ip)
                .uri(uri)
                .timestamp(LocalDateTime.now())
                .build();
        statsClient.add(hit);
    }

    private int getViews(Long eventId) {
        Map<String, Object> params = new HashMap<>();
        params.put("uri", String.format("/events/%d", eventId));
        params.put("app", "ewm-main-service");
        try {
            return (int) statsClient.getViews(params).getBody();
        } catch (NullPointerException e) {
            return 0;
        }

    }
}
