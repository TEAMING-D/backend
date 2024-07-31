package com.allin.teaming.user.domain;

import com.allin.teaming.shedule.domain.Schedule;
import com.allin.teaming.workspace.domain.Work;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    private String phone;

    private String info;

    private String major;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @OneToOne(mappedBy = "user")
    private Schedule schedule;


    @OneToMany(mappedBy = "user")
    private List<Membership> memberships = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<MeetingParticipant> meetingParticipants = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Work> works = new ArrayList<>();

    // 협업 계정 리스트
    @OneToMany(mappedBy = "user")
    private List<CollabTool> collabTools = new ArrayList<>();

    // 학교
    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    @Column(unique = true)
    private String schoolNum;

    public void update(String username, String info,
                       String major, String schoolNum) {
        this.username = username;
        this.info = info;
        this.major = major;
        this.schoolNum = schoolNum;

    }

    public void updateSchool(School school) {
        this.school = school;
    }
}
