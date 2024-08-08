package com.allin.teaming.Archive.repository;

import com.allin.teaming.Archive.domain.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    @Query("SELECT DISTINCT m FROM Material m " +
            "JOIN m.membership ms " +
            "JOIN ms.workspace ws " +
            "WHERE ws.id = :workspaceId")
    List<Material> findAllByWorkspaceId(@Param("workspaceId") Long workspaceId);

    @Query("SELECT m FROM Material m " +
            "JOIN m.membership ms " +
            "JOIN ms.user u " +
            "where u.id = :userId")
    List<Material> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT m FROM Material m " +
            "JOIN m.workMaterials wm " +
            "JOIN wm.work w " +
            "WHERE w.id = :workId")
    List<Material> findAllByWorkId(@Param("workId") Long workId);
}
