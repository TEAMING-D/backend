package com.allin.teaming.workspace.domain;

import com.allin.teaming.user.domain.Membership;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Workspace {

    @Id
    @Column(name = "workspace_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;
    private String name;
    private String description;

    public Long getId() {
        return userid;
    }

    public void setId(Long id) {
        this.userid = id;
    }

    @OneToMany(mappedBy = "workspace")
    private List<Membership> memberships = new ArrayList<>();

    @OneToMany(mappedBy = "workspace")
    private List<Meeting> meetings = new ArrayList<>();

    @OneToMany(mappedBy = "workspace")
    private List<Work> works = new ArrayList<>();

    private LocalDate created_date;

    public List<Membership> getMembers() {
        return memberships;
    }
}