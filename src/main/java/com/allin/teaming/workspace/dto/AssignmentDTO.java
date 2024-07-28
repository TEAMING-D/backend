package com.allin.teaming.workspace.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentDTO {

    private Long id;
    private Long workId;
    private Long membershipId;
    private int score;
}
