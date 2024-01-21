package ru.prackticum.comments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.prackticum.comments.utils.CommentType;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentShortDto {
    private Long id;
    private String text;
    private Long commentatorId;
    private Long eventId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private CommentType commentType;
    private int rating;
}
