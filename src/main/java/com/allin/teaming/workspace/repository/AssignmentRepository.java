package com.allin.teaming.workspace.repository;

import com.allin.teaming.workspace.domain.Assignment;
import com.allin.teaming.workspace.domain.Work;
import com.allin.teaming.user.domain.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Optional<Assignment> findByWorkAndMembership(Work work, Membership membership);
    void deleteByWork(Work work);
    List<Assignment> findByWork(Work work);
    boolean existsByWorkIdAndMembershipId(Long workId, Long membershipId);
}
