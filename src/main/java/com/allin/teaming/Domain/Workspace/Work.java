package com.allin.teaming.Domain.Workspace;

import com.allin.teaming.Domain.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity @Getter
@NoArgsConstructor
public class Work {
    @Id @Column(name = "work_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private String description;

    private LocalDate created_at;

    private LocalDate due_date;

    @Enumerated(EnumType.STRING)
    private WorkStatus workStatus;

    private double progress;
}
