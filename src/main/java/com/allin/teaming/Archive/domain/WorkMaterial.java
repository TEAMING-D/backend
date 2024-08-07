package com.allin.teaming.Archive.domain;

import com.allin.teaming.workspace.domain.Work;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkMaterial {
    @Id
    @Column(name = "work-material-id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "work_id", nullable = true)
    private Work work;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;
}
