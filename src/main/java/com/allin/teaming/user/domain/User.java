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

    @Column(name = "school_id")
    private Long schoolId;

    @Column(name = "school_name", nullable = false)
    private String schoolName;

    @Column(name = "git_id", nullable = false)
    private String gitId;

    @Column(name = "notion_mail", nullable = false)
    private String notionMail;

    @Column(name = "plus_mail", nullable = false)
    private String plusMail;

    private String major;

    private String birth;

    private String sns;

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

    public void update(String username, String phone, Long schoolId, String schoolName,
                       String gitId, String notionMail, String plusMail, String email,
                       String major, String birth, String sns) {
        this.username = username;
        this.phone = phone;
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.gitId = gitId;
        this.notionMail = notionMail;
        this.plusMail = plusMail;
        this.email = email;
        this.major = major;
        this.birth = birth;
        this.sns = sns;
    }
}
