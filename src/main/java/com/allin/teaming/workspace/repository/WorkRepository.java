package com.allin.teaming.workspace.repository;

import com.allin.teaming.workspace.domain.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {
    // 특정 워크스페이스의 업무 조회
    List<Work> findByWorkspaceId(Long workspaceId);

    // 특정 워크스페이스의 업무 조회 (업무 ID 기준)
    Optional<Work> findByIdAndWorkspaceId(Long id, Long workspaceId);
}
