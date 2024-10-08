package com.allin.teaming.workspace.repository;

import com.allin.teaming.workspace.domain.Assignment;
import com.allin.teaming.workspace.domain.Work;
import com.allin.teaming.user.domain.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Optional<Assignment> findByWorkAndMembership(Work work, Membership membership);
    void deleteByWork(Work work);
    List<Assignment> findByWork(Work work);
    boolean existsByWorkIdAndMembershipId(Long workId, Long membershipId);
    List<Assignment> findByWorkId(Long workId);

    // work_id로 Assignment 삭제
    void deleteByWorkId(Long workId);

    @Query("SELECT a FROM Assignment a WHERE a.membership.workspace.id = :workspaceId")
    List<Assignment> findByMembershipWorkspaceId(@Param("workspaceId") Long workspaceId);
}
