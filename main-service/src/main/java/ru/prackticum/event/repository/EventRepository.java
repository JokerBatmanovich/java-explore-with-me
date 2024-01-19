package ru.prackticum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.prackticum.event.model.Event;
import ru.prackticum.event.enums.State;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    Event getByIdIsAndStateIs(Long eventId, State state);

    List<Event> findAllByInitiator_Id(Long userId, Pageable pageable);

    List<Event> findAllByIdIn(List<Long> ids);
}

