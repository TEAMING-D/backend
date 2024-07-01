package com.allin.teaming.Repository.Workspace;

import com.allin.teaming.Domain.Workspace.Work;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkRepository extends JpaRepository<Work, Long> {
}
