package com.allin.teaming.workspace.dto;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class WorkspaceDTO {

    private Long id;
    private String name;
    private String location;
    private String description;

    // 기본 생성자
    public WorkspaceDTO() {
    }

    // 필드를 초기화하는 생성자
    public WorkspaceDTO(Long id, String name, String location, String description) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
    }

    // Getter 및 Setter 메서드
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
