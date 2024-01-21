package ru.prackticum.comments.rate.model;


import lombok.*;
import ru.prackticum.comments.model.Comment;
import ru.prackticum.comments.rate.utils.CommentRateId;
import ru.prackticum.user.model.User;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@EqualsAndHashCode(of = {"comment", "user"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@jakarta.persistence.Entity
@Table(name = "comments_rates")
@IdClass(CommentRateId.class)
public class CommentRate implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "comment_id", referencedColumnName = "id", nullable = false)
    private Comment comment;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "is_positive", nullable = false)
    private Boolean isPositive;

}
