package ru.prackticum.comments.rate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.prackticum.comments.rate.model.CommentRate;

public interface JpaCommentRateRepository extends JpaRepository<CommentRate, Long> {

    @Query("select cr from CommentRate as cr where cr.comment.id = :commentId and cr.user.id = :userId")
    CommentRate find(Long commentId, Long userId);

    @Modifying
    @Query("delete from CommentRate as cr where cr.comment.id = ?1 and cr.user.id = ?2")
    void delete(Long commentId, Long userId);
}
