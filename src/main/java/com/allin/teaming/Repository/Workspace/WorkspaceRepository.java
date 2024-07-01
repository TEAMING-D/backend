package com.allin.teaming.Repository.Workspace;

import com.allin.teaming.Domain.Workspace.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
}
