package ru.prackticum.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.prackticum.comments.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>,
        QuerydslPredicateExecutor<Comment> {
}
