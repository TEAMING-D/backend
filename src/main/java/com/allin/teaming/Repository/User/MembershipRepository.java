package com.allin.teaming.Repository.User;

import com.allin.teaming.Domain.User.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
}
