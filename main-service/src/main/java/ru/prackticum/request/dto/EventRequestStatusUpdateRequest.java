package ru.prackticum.request.dto;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class EventRequestStatusUpdateRequest {
    @NotEmpty
    Long[] requestIds;
    @NotNull
    @NotBlank
    String status;
}
