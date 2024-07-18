package com.allin.teaming.workspace.domain;

import com.allin.teaming.shedule.domain.Week;
import com.allin.teaming.user.domain.MeetingParticipant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Meeting {
    @Id
    @Column(name = "meeting_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    private String name;

    @Enumerated(EnumType.STRING)
    private Week week;

    private LocalTime start_time;
    private LocalTime end_time;

    private LocalDate created_at;

    @OneToMany(mappedBy = "meeting")
    private List<MeetingParticipant> meetingParticipants = new ArrayList<>();
}
