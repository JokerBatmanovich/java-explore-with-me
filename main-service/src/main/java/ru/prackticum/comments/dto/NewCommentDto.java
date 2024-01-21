package ru.prackticum.comments.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.prackticum.comments.utils.CommentType;
import ru.prackticum.comments.utils.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NotNull
public class NewCommentDto {
    @NotNull(groups = Create.class)
    @NotBlank
    @Length(min = 1, max = 300)
    private String text;
    @NotNull(groups = Create.class)
    private CommentType commentType;

}
