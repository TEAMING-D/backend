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
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
    private String title;
    private String info;
    @Enumerated(EnumType.STRING)
    private Week week;
    private LocalTime start_time;
    private LocalTime end_time;

    public void update(String title, String info, Week week,
                       LocalTime start_time, LocalTime end_time) {
        this.title = title;
        this.info = info;
        this.week = week;
        this.start_time = start_time;
        this.end_time = end_time;
    }
}
