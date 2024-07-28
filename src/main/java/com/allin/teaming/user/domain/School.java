package com.allin.teaming.user.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class School {
    @Id
    @Column(name = "school_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

}
