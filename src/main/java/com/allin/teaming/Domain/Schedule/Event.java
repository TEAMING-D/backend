package com.allin.teaming.Domain.Schedule;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
public class Event {
    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Schedule schedule;

    private String title;

    @Enumerated(EnumType.STRING)
    private Week week;

    private LocalTime start_time;

    private LocalTime end_time;
}
