package com.allin.teaming.Domain.Schedule;

import com.allin.teaming.Domain.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Schedule {

    @Id
    @Column(name = "schedule_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate created_date;

    @OneToMany(mappedBy = "schedule")
    private List<Event> events = new ArrayList<>();
}
