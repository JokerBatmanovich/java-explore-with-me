package ru.prackticum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateResult {
    @Builder.Default
    List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
    @Builder.Default
    List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

    public void addConfirmedRequest(ParticipationRequestDto request) {
        this.confirmedRequests.add(request);
    }

    public void addRejectedRequest(ParticipationRequestDto request) {
        this.rejectedRequests.add(request);
    }
}
