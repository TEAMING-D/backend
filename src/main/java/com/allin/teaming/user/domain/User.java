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

    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "git_id")
    private String gitId;

    @Column(name = "notion_mail")
    private String notionMail;

    @Column(name = "plus_mail")
    private String plusMail;

    @Column(columnDefinition = "TEXT")
    private String collabTools; // JSON 형식으로 저장

    private String major;
    private String birth;
    private String sns;

    private String info;

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

    // 학교
    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    @Column(unique = true)
    private String schoolNum;

    public void update(String username, String phone,String schoolName, String gitId,
                       String notionMail, String plusMail, String collabTools, String email,
                       String major, String birth, String sns, String info, String schoolNum) {
        this.username = username;
        this.phone = phone;
        this.schoolName = schoolName;
        this.gitId = gitId;
        this.notionMail = notionMail;
        this.plusMail = plusMail;
        this.collabTools = collabTools;
        this.email = email;
        this.major = major;
        this.birth = birth;
        this.sns = sns;
        this.info = info;
        this.schoolNum = schoolNum;
    }

    public void updateSchool(School school) {
        this.school = school;
    }
}