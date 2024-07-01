package com.allin.teaming.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Event {
    @Id
    @Column(name = "event_id")
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Schedule schedule;

    private String title;

    @Enumerated(EnumType.STRING)
    private Week week;

    private LocalTime start_time;

    private LocalTime end_time;
}
