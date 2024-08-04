package com.allin.teaming.workspace.repository;

import com.allin.teaming.workspace.domain.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {

    @Query("SELECT w FROM Work w WHERE w.workspace.id = :workspaceId")
    List<Work> findByWorkspaceId(@Param("workspaceId") Long workspaceId);
    Optional<Work> findByIdAndWorkspaceId(Long id, Long workspaceId);
}
