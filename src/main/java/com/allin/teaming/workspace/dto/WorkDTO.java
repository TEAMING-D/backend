package com.allin.teaming.workspace.dto;

import com.allin.teaming.user.dto.UserDto;
import com.allin.teaming.workspace.domain.WorkStatus;
import com.allin.teaming.workspace.domain.Work;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class WorkDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate createdAt;
    private LocalDate dueDate;
    private WorkStatus status;
    private double progress;
    private int weight = 1;
    private List<Long> assignedUserIds;
    private List<UserDto.UserAssignmentDto> assignedUsers;

    public WorkDTO() {}

    public WorkDTO(Work work) {
        this.id = work.getId();
        this.name = work.getName();
        this.description = work.getDescription();
        this.createdAt = work.getCreated_at();
        this.dueDate = work.getDue_date();
        this.status = work.getStatus();
        this.progress = work.getProgress();
        this.weight = work.getWeight();
    }
}
