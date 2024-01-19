package ru.prackticum.compilation.model;

import lombok.*;
import ru.prackticum.event.model.Event;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Entity
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToMany
    @JoinTable(
            name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    List<Event> events;
    @Column
    Boolean pinned;
    @Column(unique = true)
    String title;
}
