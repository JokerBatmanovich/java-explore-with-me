package ru.prackticum.comments.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.prackticum.comments.model.Comment;
import ru.prackticum.event.model.Event;
import ru.prackticum.user.dto.UserMapper;
import ru.prackticum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    final private UserMapper userMapper;
    public Comment toEntity(NewCommentDto newComment, Event event, User user) {
        return Comment.builder()
                .commentator(user)
                .event(event)
                .text(newComment.getText())
                .commentType(newComment.getCommentType())
                .createdOn(LocalDateTime.now())
                .build();
    }

    public Comment toEntity(NewCommentDto newComment, Event event, User user, Comment replyTo) {
        return Comment.builder()
                .commentator(user)
                .event(event)
                .text(newComment.getText())
                .replyTo(replyTo)
                .commentType(newComment.getCommentType())
                .createdOn(LocalDateTime.now())
                .build();
    }

    public CommentShortDto toShortDto(Comment comment) {
        return CommentShortDto.builder()
                .id(comment.getId())
                .commentatorId(comment.getCommentator() != null ? (comment.getCommentator().getId())
                                                                : null)
                .eventId(comment.getEvent().getId())
                .text(comment.getText())
                .commentType(comment.getCommentType())
                .createdOn(comment.getCreatedOn())
                .rating(comment.getRating())
                .build();
    }

    public List<CommentShortDto> toShortDtoList(List<Comment> comments) {
        return comments.stream().map(this::toShortDto).collect(Collectors.toList());
    }

    public CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .commentator(comment.getCommentator() != null ? userMapper.toShortDto(comment.getCommentator())
                                                              : null)
                .eventId(comment.getEvent().getId())
                .text(comment.getText())
                .commentType(comment.getCommentType())
                .replyTo(comment.getReplyTo() != null ? toShortDto(comment.getReplyTo())
                                                      : null)
                .createdOn(comment.getCreatedOn())
                .rating(comment.getRating())
                .build();
    }

    public List<CommentDto> toDtoList(List<Comment> comments) {
        return comments.stream().map(this::toDto).collect(Collectors.toList());
    }


}
