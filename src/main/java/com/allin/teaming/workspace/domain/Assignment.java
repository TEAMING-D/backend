package com.allin.teaming.workspace.domain;

import com.allin.teaming.user.domain.Membership;
import com.allin.teaming.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor@Table(name = "assignment", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"work_id", "membership_id"})
})
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "work_id")
    private Work work;

    @ManyToOne
    @JoinColumn(name = "membership_id")
    private Membership membership;

    private int score;

    public Assignment(Membership membership, Work work) {
        this.membership = membership;
        this.work = work;
    }
}