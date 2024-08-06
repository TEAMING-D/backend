package com.allin.teaming.workspace.domain;

import com.allin.teaming.shedule.domain.Week;
import com.allin.teaming.user.domain.MeetingParticipant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meeting {
    @Id
    @Column(name = "meeting_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    private String title;

    @Enumerated(EnumType.STRING)
    private Week week;

    private LocalTime start_time;
    private LocalTime end_time;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingParticipant> meetingParticipants = new ArrayList<>();

    @Builder.Default
    private boolean complete = false;

    public Meeting complete() {
        this.complete = true;
        return this;
    }

    public void updateTime(Week week, LocalTime start_time, LocalTime end_time) {
        this.week = week;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public void updateTitle(String title) {
        this.title = title;
    }
}
