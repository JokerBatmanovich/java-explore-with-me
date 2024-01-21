package ru.prackticum.comments.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.prackticum.comments.utils.CommentType;
import ru.prackticum.event.model.Event;
import ru.prackticum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@jakarta.persistence.Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String text;
    @Column
    private int rating = 0;
    @Column(name = "created_on")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @ManyToOne
    @JoinColumn(name = "commentator_id")
    private User commentator;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "reply_to_id")
    private Comment replyTo;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CommentType commentType;

    public void increaseRating(int i) {
        setRating(rating + i);
    }

    public void decreaseRating(int i) {
        setRating(rating - i);
    }
}
