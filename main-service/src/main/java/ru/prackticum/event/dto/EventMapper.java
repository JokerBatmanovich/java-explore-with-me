package ru.prackticum.event.dto;

import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import ru.prackticum.catergory.dto.CategoryDto;
import ru.prackticum.catergory.model.Category;
import ru.prackticum.event.enums.State;
import ru.prackticum.event.model.Event;
import ru.prackticum.user.dto.UserShortDto;
import ru.prackticum.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
//@Mapper(componentModel = "spring")
public class EventMapper {

    public Event toEntity(NewEventDto newEventDto, Category category, User user) {
        return Event.builder()
                .title(newEventDto.getTitle())
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .category(category)
                .participantLimit(newEventDto.getParticipantLimit())
                .paid(newEventDto.getPaid())
                .requestModeration(newEventDto.getRequestModeration())
                .location(newEventDto.getLocation())
                .createdOn(LocalDateTime.now())
                .initiator(user)
                .state(State.PENDING)
                .build();
    }

    public EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .initiator(UserShortDto.builder()
                        .id(event.getInitiator().getId())
                        .name(event.getInitiator().getName())
                        .build())
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .confirmedRequests(event.getConfirmedRequests())
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .eventDate(event.getEventDate())
                .build();
    }

    public List<EventShortDto> toShortDtoList(List<Event> events) {
        return events.stream().map(this::toShortDto).collect(Collectors.toList());
    }

    public EventFullDto toFullDto(Event event) {

        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .category(CategoryDto.builder()
                        .id(event.getCategory().getId())
                        .name(event.getCategory().getName())
                        .build())
                .participantLimit(event.getParticipantLimit())
                .paid(event.getPaid())
                .requestModeration(event.getRequestModeration())
                .publishedOn(event.getPublishedOn())
                .location(event.getLocation())
                .createdOn(LocalDateTime.now())
                .initiator(UserShortDto.builder()
                        .id(event.getInitiator().getId())
                        .name(event.getInitiator().getName())
                        .build())
                .state(event.getState())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .build();
    }

    public List<EventFullDto> toFullDtoList(List<Event> events) {
        return events.stream().map(this::toFullDto).collect(Collectors.toList());
    }

    public void updateEventFromRequest(UpdateEventRequest adminRequest, @MappingTarget Event event) {
        if (adminRequest.getAnnotation() != null) {
            event.setAnnotation(adminRequest.getAnnotation());
        }
        if (adminRequest.getTitle() != null) {
            event.setTitle(adminRequest.getTitle());
        }
        if (adminRequest.getDescription() != null) {
            event.setDescription(adminRequest.getDescription());
        }
        if (adminRequest.getEventDate() != null) {
            event.setEventDate(adminRequest.getEventDate());
        }
        if (adminRequest.getLocation() != null) {
            event.setLocation(adminRequest.getLocation());
        }
        if (adminRequest.getPaid() != null) {
            event.setPaid(adminRequest.getPaid());
        }
        if (adminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(adminRequest.getParticipantLimit());
        }
        if (adminRequest.getRequestModeration() != null) {
            event.setRequestModeration(adminRequest.getRequestModeration());
        }
    }

}
