package com.allin.teaming.Domain.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollabTool {
    @Id
    @Column(name = "colab_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String toolName;
    private String account;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
