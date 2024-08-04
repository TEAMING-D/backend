package com.allin.teaming.user.repository;

import com.allin.teaming.user.domain.Membership;
import com.allin.teaming.user.domain.User;
import com.allin.teaming.workspace.domain.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    boolean existsByWorkspaceIdAndUserId(Long workspaceId, Long userId);
    Optional<Membership> findByUserIdAndWorkspaceId(Long userId, Long workspaceId);
    Optional<Membership> findByUserAndWorkspace(User user, Workspace workspace);
    List<Membership> findAllByUser(User user);
    Optional<Membership> findByUserId(Long userId);
    void deleteById(Long id);

    @Modifying
    @Query("DELETE FROM Membership m WHERE m.workspace = :workspace")
    void deleteAllByWorkspace(Workspace workspace);
}
