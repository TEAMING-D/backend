package com.allin.teaming.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@NoArgsConstructor
public class MeetingParticipant {
    @Id @Column(name = "meeting_participant_id")
    @GeneratedValue
    private Long id;

    @OneToMany
    private User user;

    @OneToMany
    private Meeting meeting;
}
