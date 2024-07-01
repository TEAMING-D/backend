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
public class Workspace {

    @Id
    @Column(name = "workspace_id")
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "workspace")
    private List<Membership> memberships = new ArrayList<>();

    @OneToMany(mappedBy = "workspace")
    private List<Meeting> meetings = new ArrayList<>();

    private LocalDate created_date;

    private String name;
}
