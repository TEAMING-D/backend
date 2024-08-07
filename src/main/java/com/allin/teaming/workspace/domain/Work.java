package com.allin.teaming.workspace.domain;

import com.allin.teaming.Archive.domain.WorkMaterial;
import com.allin.teaming.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_id")
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
    private WorkStatus status;
    private double progress;
    private int weight = 1;

    @OneToMany(mappedBy = "work")
    private List<WorkMaterial> workMaterials = new ArrayList<>();

}
