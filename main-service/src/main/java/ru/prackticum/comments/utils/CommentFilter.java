package ru.prackticum.comments.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentFilter {
    LocalDateTime start;
    LocalDateTime end;
    Long eventId;
    Long userId;
    Long repliedCommentId;
}
