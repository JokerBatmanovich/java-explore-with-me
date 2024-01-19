package ru.prackticum.event.service;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.prackticum.event.enums.State;
import ru.prackticum.event.model.Event;
import ru.prackticum.event.repository.EventRepository;
import ru.prackticum.event.utils.EventFilter;
import ru.prackticum.event.utils.QPredicates;
import ru.prackticum.exception.EventNotFoundException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static ru.prackticum.event.model.QEvent.event;


@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{
    private final EventRepository eventRepository;

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event getByIdAndState(Long eventId, State state) {
        return eventRepository.getByIdIsAndStateIs(eventId, state);
    }

    @Override
    public Event getById(Long eventId) {
        try {
            Event event = eventRepository.getReferenceById(eventId);
            System.out.println(event);
            return event;
        } catch (EntityNotFoundException e) {
            throw new EventNotFoundException(eventId);
        }
    }

    @Override
    public List<Event> getByIds(List<Long> ids) {
        return eventRepository.findAllByIdIn(ids);
    }

    @Override
    public List<Event> getUserEvents(Long userId, Pageable pageable) {
        return eventRepository.findAllByInitiator_Id(userId, pageable);
    }

    @Override
    public List<Event> getEventsByFilter(EventFilter eventFilter, Pageable pageable) {
        if (eventFilter.isEmpty()) {
            return eventRepository.findAll(pageable).toList();
        } else {
            Predicate predicate = buildPredicate(eventFilter);
            return eventRepository.findAll(predicate, pageable).toList();
        }
    }

    private Predicate buildPredicate(EventFilter eventFilter) {
        Predicate textPredicate = QPredicates.builder()
                .add(eventFilter.getText(), event.annotation::containsIgnoreCase)
                .add(eventFilter.getText(), event.description::containsIgnoreCase)
                .buildOr();
        QPredicates predicates = QPredicates.builder()
                .add(eventFilter.getCategories(), event.category.id::in)
                .add(eventFilter.getUsers(), event.initiator.id::in)
                .add(eventFilter.getStates(), event.state::in)
                .add(eventFilter.getRangeStart(), event.eventDate::after)
                .add(eventFilter.getRangeEnd(), event.eventDate::before)
                .add(textPredicate)
                .add(eventFilter.getPaid(), event.paid::eq);
        if (eventFilter.getOnlyAvailable() != null) {
            if (eventFilter.getOnlyAvailable()) {
                predicates.add(event.confirmedRequests, event.participantLimit::eq);
            } else {
                predicates.add(event.confirmedRequests, event.participantLimit::goe);
            }
        }
        return predicates.buildAnd();
    }
}
