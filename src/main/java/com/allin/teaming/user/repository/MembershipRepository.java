package com.allin.teaming.user.repository;

import com.allin.teaming.user.domain.Membership;
import com.allin.teaming.user.domain.User;
import com.allin.teaming.workspace.domain.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Optional<Membership> findByUserAndWorkspace(User user, Workspace workspace);
    List<Membership> findAllByUser(User user);
    void deleteById(Long id);

}
