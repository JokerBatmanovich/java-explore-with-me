package ru.prackticum.comments.service;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.prackticum.comments.model.Comment;
import ru.prackticum.comments.rate.model.CommentRate;
import ru.prackticum.comments.rate.repository.JpaCommentRateRepository;
import ru.prackticum.comments.repository.CommentRepository;
import ru.prackticum.comments.utils.CommentFilter;
import ru.prackticum.event.utils.QPredicates;
import ru.prackticum.exception.CommentNotFoundException;
import ru.prackticum.user.model.User;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

import static ru.prackticum.comments.model.QComment.comment;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    final private CommentRepository commentRepository;
    final private JpaCommentRateRepository rateRepository;

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment getById(Long commentId) {
        try {
            Comment comment = commentRepository.getReferenceById(commentId);
            System.out.println(comment);
            return comment;
        } catch (EntityNotFoundException e) {
            throw new CommentNotFoundException(commentId);
        }
    }

    @Override
    public List<Comment> getCommentsByParams(CommentFilter filter, Pageable pageable) {
        Predicate predicate = buildPredicate(filter);
        return commentRepository.findAll(predicate, pageable).toList();
    }

    private Predicate buildPredicate(CommentFilter filter) {
        QPredicates predicates = QPredicates.builder()
                .add(filter.getEventId(), comment.event.id::eq)
                .add(filter.getStart(), comment.createdOn::after)
                .add(filter.getEnd(), comment.createdOn::before)
                .add(filter.getUserId(), comment.commentator.id::eq)
                .add(filter.getRepliedCommentId(), comment.replyTo.id::eq);

        return predicates.buildAnd();
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentRate getRate(Long userId, Long commentId) {
        return rateRepository.find(commentId, userId);
    }

    @Transactional
    @Override
    public void setRate(User user, Comment comment, Boolean isPositive) {
        if (isPositive != null)  {
            rateRepository.save(CommentRate.builder()
                            .user(user)
                            .comment(comment)
                            .isPositive(isPositive)
                            .build());
        } else {
            rateRepository.delete(comment.getId(), user.getId());
        }
    }
}
