package com.allin.teaming.workspace.dto;

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
    private WorkStatus Status;
    private double progress;
    private int weight=1;
    private List<Long> assignedUserIds;

    public WorkDTO() {}

    public WorkDTO(Work work) {
        this.id = work.getId();
        this.name = work.getName();
        this.description = work.getDescription();
        this.createdAt = work.getCreated_at();
        this.dueDate = work.getDue_date();
        this.Status = work.getStatus();
        this.progress = work.getProgress();
        this.weight = work.getWeight();
    }
}
