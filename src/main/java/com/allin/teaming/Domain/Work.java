package com.allin.teaming.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity @Getter
@NoArgsConstructor
public class Work {
    @Id @Column(name = "work_id")
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Workspace workspace;

    @ManyToOne
    private User user;

    private String name;

    private String description;

    private LocalDate created_at;

    private LocalDate due_date;

    @Enumerated(EnumType.STRING)
    private WorkStatus workStatus;

    private double progress;
}
