package ru.prackticum.model;

import lombok.*;
import javax.persistence.*;

import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hits")
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String app;
    @Column
    private String uri;
    @Column
    private String ip;
    @Column
    private LocalDateTime timestamp;
}
