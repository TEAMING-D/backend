package com.allin.teaming.Repository.User;

import com.allin.teaming.Domain.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
