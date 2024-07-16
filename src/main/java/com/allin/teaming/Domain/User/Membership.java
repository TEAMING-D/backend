package com.allin.teaming.Domain.User;

import com.allin.teaming.Domain.Workspace.Workspace;
import jakarta.persistence.*;
import lombok.*;

@Entity @Getter
@NoArgsConstructor
@AllArgsConstructor
public class Membership {

    @Id @Column(name = "membership_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProjectRole role;

    private double score;

    @ManyToOne
    @Setter
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

}
