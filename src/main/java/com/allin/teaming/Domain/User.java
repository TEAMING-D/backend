package com.allin.teaming.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue
    private Long id;

    private String email;

    private String password;

    private String username;

    private String snsId;

    private String info;

    @OneToOne(mappedBy = "user")
    private Schedule schedule;

    @OneToMany(mappedBy = "user")
    private List<Membership> memberships = new ArrayList<>();
}
