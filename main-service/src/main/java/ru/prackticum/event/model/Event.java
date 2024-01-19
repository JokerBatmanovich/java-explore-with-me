package ru.prackticum.event.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.prackticum.catergory.model.Category;
import ru.prackticum.catergory.model.Location;
import ru.prackticum.event.enums.State;
import ru.prackticum.user.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Entity
@Entity
@Table(name = "events")
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String annotation;
    @Column
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column
    private int confirmedRequests;
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @Column
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @Embedded
    private Location location;
    @Column
    private Boolean paid;
    @Column
    private Integer participantLimit;
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    @Column
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private State state;
    @Column
    private int views;

    public void increaseRequests() {
        this.confirmedRequests++;
    }
}
