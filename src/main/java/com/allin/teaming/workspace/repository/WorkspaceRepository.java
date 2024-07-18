package com.allin.teaming.workspace.repository;

import com.allin.teaming.workspace.domain.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
}
