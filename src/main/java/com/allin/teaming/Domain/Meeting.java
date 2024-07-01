package com.allin.teaming.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@NoArgsConstructor
public class Meeting {
    @Id
    @Column(name = "meeting_id")
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Workspace workspace;

    private String name;
}
