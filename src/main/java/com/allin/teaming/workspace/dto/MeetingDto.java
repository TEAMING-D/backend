package com.allin.teaming.workspace.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class MeetingDTO {

    private Long id;
    private String title;  // 이름에 맞게 수정
    private String description; // 이 필드가 존재해야 합니다요시 추가
    private LocalDateTime startTime;  // 필드에 맞게 수정
    private LocalDateTime endTime;    // 필드에 맞게 수정
    private Long workspaceId;

    // 기본 생성자
    public MeetingDTO() {
    }

    // 필드를 초기화하는 생성자
    public MeetingDTO(Long id, String title, String description, LocalDateTime startTime, LocalDateTime endTime, Long workspaceId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.workspaceId = workspaceId;
    }

    // Getter 및 Setter 메서드
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }
}
