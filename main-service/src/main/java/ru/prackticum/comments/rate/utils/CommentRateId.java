package ru.prackticum.comments.rate.utils;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"comment", "user"})
public class CommentRateId implements Serializable {

    private Long comment;
    private Long user;

}
