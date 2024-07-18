package com.allin.teaming.workspace.domain;

import com.allin.teaming.user.domain.Membership;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Workspace {

    @Id
    @Column(name = "workspace_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "workspace")
    private List<Membership> memberships = new ArrayList<>();

    @OneToMany(mappedBy = "workspace")
    private List<Meeting> meetings = new ArrayList<>();

    @OneToMany(mappedBy = "workspace")
    private List<Work> works = new ArrayList<>();

    private LocalDate created_date;

    private String name;
}
