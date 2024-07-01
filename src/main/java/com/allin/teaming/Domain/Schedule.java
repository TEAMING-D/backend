package com.allin.teaming.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Schedule {

    @Id
    @Column(name = "schedule_id")
    @GeneratedValue
    private Long id;

    private String title;

    @OneToOne
    private User user;

    private LocalDate created_date;

    @OneToMany
    private List<Event> events = new ArrayList<>();
}
