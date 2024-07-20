package com.allin.teaming.workspace.domain;

import com.allin.teaming.shedule.domain.Week;
import com.allin.teaming.user.domain.MeetingParticipant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Meeting {
    private String description;

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

    private LocalTime startTime;  // StartTime
    private LocalTime endTime;    // EndTime

    private LocalDate createdAt;
    @OneToMany(mappedBy = "meeting")
    private List<MeetingParticipant> meetingParticipants = new ArrayList<>();

    @Getter
    public String getDescription() {
        return description;
    }

    @Setter
    public void setDescription(String description) {
        this.description = description;
    }
}
