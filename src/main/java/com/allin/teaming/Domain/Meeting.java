package com.allin.teaming.Domain;

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
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Workspace workspace;

    private String name;

    @Enumerated(EnumType.STRING)
    private Week week;

    private LocalTime start_time;
    private LocalTime end_time;

    private LocalDate created_at;

    @OneToMany(mappedBy = "meeting_id")
    private List<MeetingParticipant> meetingParticipants = new ArrayList<>();
}
