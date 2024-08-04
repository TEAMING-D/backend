package com.allin.teaming.user.domain;

import com.allin.teaming.workspace.domain.Workspace;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "membership", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "workspace_id"})
})
public class Membership {

    @Id
    @Column(name = "membership_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberName;

    @Enumerated(EnumType.STRING)
    private ProjectRole role;

    private double score;

    @ManyToOne
    @Setter
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    public Membership(User user, Workspace workspace) {
        this.user = user;
        this.workspace = workspace;
    }
}
