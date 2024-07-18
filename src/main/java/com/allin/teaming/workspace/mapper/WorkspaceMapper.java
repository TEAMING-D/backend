package com.allin.teaming.workspace.mapper;

import com.allin.teaming.workspace.domain.Workspace;
import com.allin.teaming.workspace.dto.WorkspaceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WorkspaceMapper {

    WorkspaceMapper INSTANCE = Mappers.getMapper(WorkspaceMapper.class);

    @Mapping(target = "id", ignore = true)
    Workspace toEntity(WorkspaceDTO dto);

    WorkspaceDTO toDTO(Workspace entity);

    void updateFromDTO(WorkspaceDTO dto, @MappingTarget Workspace entity);
}
