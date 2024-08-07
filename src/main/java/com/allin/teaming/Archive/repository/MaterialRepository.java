package com.allin.teaming.Archive.repository;

import com.allin.teaming.Archive.domain.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    @Query("SELECT DISTINCT m FROM Material m " +
            "JOIN m.workMaterials wm " +
            "JOIN wm.work w " +
            "JOIN w.workspace ws " +
            "WHERE ws.id = :workspaceId")
    List<Material> findAllByWorkspaceId(@Param("workspaceId") Long workspaceId);
}
