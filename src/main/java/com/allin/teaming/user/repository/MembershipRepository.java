package com.allin.teaming.user.repository;

import com.allin.teaming.user.domain.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
}
