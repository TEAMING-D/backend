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
    private Long id;
    private String name;
    private String description;

    // 새로운 필드 추가
    private String type; // 예: 수업, 대회, 동아리, 기타
    private LocalDate deadline; // 마감 기한

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
