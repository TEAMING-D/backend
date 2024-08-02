package com.allin.teaming.workspace.domain;

import com.allin.teaming.user.domain.Membership;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workspace {

    @Id
    @Column(name = "workspace_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String type; // 예: 수업, 대회, 동아리, 기타

    private LocalDate deadline;
    private LocalDate created_date;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> memberships = new ArrayList<>();

    @OneToMany(mappedBy = "workspace")
    private List<Meeting> meetings = new ArrayList<>();

    @OneToMany(mappedBy = "workspace")
    private List<Work> works = new ArrayList<>();

    public List<Membership> getMembers() {
        return memberships;
    }
}
