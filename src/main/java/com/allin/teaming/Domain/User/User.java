package com.allin.teaming.Domain.User;

import com.allin.teaming.Domain.Schedule.Schedule;
import com.allin.teaming.Domain.Workspace.Work;
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

    private String password;

    @Column(nullable = false)
    private String username;

    // 계정 테이블 추가할까
    private String notionId;
    private String githubId;
    private String emailId;

    private String info;

    private String school;
    private String major;

    @OneToOne(mappedBy = "user")
    private Schedule schedule;

    @OneToMany(mappedBy = "user")
    private List<Membership> memberships = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<MeetingParticipant> meetingParticipants = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Work> works = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<CollabTool> collabTools = new ArrayList<>();
}
