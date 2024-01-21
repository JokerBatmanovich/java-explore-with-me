package ru.prackticum.comments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.prackticum.comments.utils.CommentType;
import ru.prackticum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    private Long id;
    private String text;
    private UserShortDto commentator;
    private Long eventId;
    private CommentShortDto replyTo;
    private CommentType commentType;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private int rating;
}
