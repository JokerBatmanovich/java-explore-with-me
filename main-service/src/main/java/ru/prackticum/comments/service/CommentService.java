package ru.prackticum.comments.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.prackticum.comments.model.Comment;
import ru.prackticum.comments.rate.model.CommentRate;
import ru.prackticum.comments.utils.CommentFilter;
import ru.prackticum.user.model.User;

import java.util.List;

@Service
public interface CommentService {
    Comment save(Comment comment);

    Comment getById(Long commentId);

    List<Comment> getCommentsByParams(CommentFilter filter, Pageable pageable);

    void deleteComment(Long commentId);

    CommentRate getRate(Long userId, Long commentId);

    void setRate(User user, Comment commentId, Boolean isPositive);

}
