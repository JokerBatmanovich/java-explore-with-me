package ru.prackticum.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.prackticum.request.util.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Entity
@Entity
@Table(name = "requests")
public class ParticipationRequestDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created;
    @Column(name = "requester_id")
    Long requester;
    @Enumerated(EnumType.STRING)
    Status status;
    @Column(name = "event_id")
    Long event;


}
