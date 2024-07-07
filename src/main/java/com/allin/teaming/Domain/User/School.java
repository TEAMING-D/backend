package com.allin.teaming.Domain.User;

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
    private String location;
    private String otherInfo;
}
