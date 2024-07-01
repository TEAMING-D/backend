package com.allin.teaming.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@NoArgsConstructor
public class Membership {

    @Id @Column(name = "membership_id")
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    private double score;

    @ManyToOne
    private User user;

    @ManyToOne
    private Workspace workspace;
}
