package ru.prackticum.comments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.prackticum.comments.dto.CommentDto;
import ru.prackticum.comments.dto.CommentMapper;
import ru.prackticum.comments.dto.CommentShortDto;
import ru.prackticum.comments.dto.NewCommentDto;
import ru.prackticum.comments.model.Comment;
import ru.prackticum.comments.rate.model.CommentRate;
import ru.prackticum.comments.service.CommentService;
import ru.prackticum.comments.utils.CommentFilter;
import ru.prackticum.comments.utils.Create;
import ru.prackticum.event.enums.State;
import ru.prackticum.event.model.Event;
import ru.prackticum.event.service.EventService;
import ru.prackticum.exception.ConflictException;
import ru.prackticum.exception.IncorrectParameterException;
import ru.prackticum.exception.NoPermissionsException;
import ru.prackticum.user.model.User;
import ru.prackticum.user.service.UserService;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping
@Validated
@RequiredArgsConstructor
public class CommentController {
    @Qualifier("commentServiceImpl")
    final private CommentService commentService;
    @Qualifier("userServiceImpl")
    final private UserService userService;
    @Qualifier("eventServiceImpl")
    final private EventService eventService;
    final private CommentMapper commentMapper;

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @PostMapping("/users/{userId}/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto add(@PathVariable Long userId,
                          @PathVariable Long eventId,
                          @RequestParam(name = "replyTo", required = false) Long replyTo,
                          @RequestBody @Validated({Create.class}) NewCommentDto newComment) {
        User user = userService.getById(userId);
        Event event = eventService.getById(eventId);
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("Нельзя прокомментировать неопубликованное событие");
        }
        Comment comment;
        if (replyTo != null) {
            Comment commentToReply = commentService.getById(replyTo);
            if (!Objects.equals(commentToReply.getEvent().getId(), eventId)) {
                throw new ConflictException("Нельзя ответить на комментарий, не относящийся к этому событию.");
            } else {
                comment = commentMapper.toEntity(newComment, event, user, commentService.getById(replyTo));
            }
        } else {
            comment = commentMapper.toEntity(newComment, event, user);
        }
        return commentMapper.toDto(commentService.save(comment));
    }

    @GetMapping("/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentShortDto> getEventsComments(@PathVariable Long eventId,
                                            @RequestParam(name = "sort", defaultValue = "CREATED_ON_DESC") String sort,
                                            @RequestParam(name = "rangeStart", required = false) String startParam,
                                            @RequestParam(name = "rangeEnd", required = false) String endParam,
                                            @RequestParam(name = "from", defaultValue = "0") @Min(0) Long from,
                                            @RequestParam(name = "size", defaultValue = "10")  @Min(1) Integer size) {
        Event event = eventService.getById(eventId);

        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("У этого события нет комментариев, т.к. оно неопубликовано.");
        }

        CommentFilter filter = fillFilter(startParam, endParam);
        filter.setEventId(eventId);

        return commentMapper.toShortDtoList(commentService.getCommentsByParams(filter, getPageable(sort, from, size)));
    }

    @GetMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getCommentById(@PathVariable Long commentId) {
        return commentMapper.toDto(commentService.getById(commentId));
    }

    @GetMapping({"/users/{userId}/comments", "/admin/users/{userId}/comments"})
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getUserCommentsByUser(@PathVariable Long userId,
                                            @RequestParam(name = "sort", defaultValue = "CREATED_ON_DESC") String sort,
                                            @RequestParam(name = "rangeStart", required = false) String startParam,
                                            @RequestParam(name = "rangeEnd", required = false) String endParam,
                                            @RequestParam(name = "from", defaultValue = "0") @Min(0) Long from,
                                            @RequestParam(name = "size", defaultValue = "10")  @Min(1) Integer size) {

        userService.getById(userId);

        CommentFilter filter = fillFilter(startParam, endParam);
        filter.setUserId(userId);

        return commentMapper.toDtoList(commentService.getCommentsByParams(filter, getPageable(sort, from, size)));
    }

    @GetMapping("/comments/{commentId}/replies")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentShortDto> getCommentReplies(@PathVariable Long commentId,
                                            @RequestParam(name = "sort", defaultValue = "CREATED_ON_DESC") String sort,
                                            @RequestParam(name = "rangeStart", required = false) String startParam,
                                            @RequestParam(name = "rangeEnd", required = false) String endParam,
                                            @RequestParam(name = "from", defaultValue = "0") @Min(0) Long from,
                                            @RequestParam(name = "size", defaultValue = "10")  @Min(1) Integer size) {
        commentService.getById(commentId);

        CommentFilter filter = fillFilter(startParam, endParam);
        filter.setRepliedCommentId(commentId);

        return commentMapper.toShortDtoList(commentService.getCommentsByParams(filter, getPageable(sort, from, size)));
    }


    @DeleteMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByUser(@PathVariable Long userId,
                                    @PathVariable Long commentId) {
        userService.getById(userId);
        checkPermission(userId,commentId);
        commentService.deleteComment(commentId);
    }

    @DeleteMapping("/admin/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(@PathVariable Long commentId) {
        commentService.getById(commentId);
        commentService.deleteComment(commentId);
    }

    @PatchMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateCommentByUser(@PathVariable Long userId,
                                          @PathVariable Long commentId,
                                          @RequestBody NewCommentDto updateComment) {
        userService.getById(userId);
        Comment comment = checkPermission(userId, commentId);
        if (updateComment.getText() != null) {
            comment.setText(updateComment.getText());
        }
        if (updateComment.getCommentType() != null) {
            comment.setCommentType(updateComment.getCommentType());
        }

        return commentMapper.toDto(commentService.save(comment));
    }

    @PatchMapping("/users/{userId}/comments/{commentId}/rate")
    @ResponseStatus(HttpStatus.OK)
    public void rateComment(@PathVariable Long userId,
                            @PathVariable Long commentId,
                            @RequestParam(name = "isPositive", required = false) Boolean isPositive) {

        User user = userService.getById(userId);
        Comment comment = commentService.getById(commentId);
        if (comment.getCommentator().getId().equals(userId)) {
            throw new ConflictException("Нельзя оценить свой комментарий");
        }

        CommentRate commentRate = commentService.getRate(userId, commentId);
        if (commentRate == null) {
            if (isPositive == null) {               //cR=iP=null
                throw new ConflictException("Нельзя отменить оценку, которую вы не ставили.");
            } else {
                if (isPositive) {                   //cR=null, iP=true
                    comment.increaseRating(1);
                } else {                            //cR=null, iP=false
                    comment.decreaseRating(1);
                }

            }
        } else {
            if (commentRate.getIsPositive() == isPositive) { //cR=iP!=null
                throw new ConflictException("Вы уже поставили эту оценку этому комментарию.");
            } else {
                if(isPositive == null){
                    if (commentRate.getIsPositive()){  //cR=true, iP=null
                        comment.decreaseRating(1);
                    } else {                           //cR=false, iP=null
                        comment.increaseRating(1);
                    }
                } else if (isPositive.equals(true)) {  //cR=false, iP=true
                    comment.increaseRating(2);
                } else if (isPositive.equals(false)) { //cR=true, iP=false
                    comment.decreaseRating(2);
                }
            }
        }
        comment = commentService.save(comment);
        commentService.setRate(user, comment, isPositive);
    }

    private CommentFilter fillFilter(String startParam, String endParam) {
        CommentFilter filter = new CommentFilter();
        if (startParam != null && !startParam.isBlank()) {
            filter.setStart(LocalDateTime.parse(startParam, formatter));
        }
        if (endParam != null && !endParam.isBlank()) {
            filter.setEnd(LocalDateTime.parse(endParam, formatter));
        }

        if (filter.getStart() != null && filter.getEnd() != null && filter.getStart().isAfter(filter.getEnd())) {
            throw new IncorrectParameterException("rangeStart не может быть позже rangeEnd");
        }
        return filter;
    }

    private Pageable getPageable(String sort, Long from, Integer size) {
        Pageable pageable;
        switch (sort) {
            case "CREATED_ON_DESC":
                pageable = PageRequest.of(Math.toIntExact(from / size), size,
                        Sort.by("createdOn").descending());
                break;
            case "CREATED_ON_ASC":
                pageable = PageRequest.of(Math.toIntExact(from / size), size,
                        Sort.by("createdOn").ascending());
                break;
            case "RATING":
                pageable = PageRequest.of(Math.toIntExact(from / size), size,
                        Sort.by("rating").descending());
                break;
            default:
                throw new IncorrectParameterException("Неверное значение параметра sort. Доступные значения: " +
                        "CREATED_ON_DESC, CREATED_ON_ASC, RATING");
        }
        return pageable;
    }

    private Comment checkPermission(Long userId, Long commentId) {
        Comment comment = commentService.getById(commentId);
        if (!Objects.equals(comment.getCommentator().getId(), userId)) {
            throw new NoPermissionsException(String.format("У пользователя с ID=%d нет прав " +
                    "на удаление/редактирование комментария с ID=%d", userId, commentId));
        }
        return comment;
    }

}
