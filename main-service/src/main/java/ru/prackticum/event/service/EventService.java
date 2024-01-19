package ru.prackticum.event.service;

import ru.prackticum.event.model.Event;
import ru.prackticum.event.enums.State;
import org.springframework.data.domain.Pageable;
import ru.prackticum.event.utils.EventFilter;

import java.util.List;

public interface EventService {

    Event save(Event event);

    Event getById(Long eventId);

    List<Event> getByIds(List<Long> ids);

    Event getByIdAndState(Long eventId, State state);

    List<Event> getUserEvents(Long userId, Pageable pageable);

    List<Event> getEventsByFilter(EventFilter eventFilter, Pageable pageable);


}
