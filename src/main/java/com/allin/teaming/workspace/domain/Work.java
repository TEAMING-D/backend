package com.allin.teaming.workspace.domain;

import com.allin.teaming.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity @Getter @Setter
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
    private WorkStatus Status;

    private double progress;
}
