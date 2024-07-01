package com.allin.teaming.Domain.User;

import com.allin.teaming.Domain.Workspace.Workspace;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Getter
@NoArgsConstructor
public class Membership {

    @Id @Column(name = "membership_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    private double score;

    @ManyToOne
    @Setter
    private User user;

    @ManyToOne
    private Workspace workspace;

}
