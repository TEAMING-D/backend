package com.allin.teaming.user.repository;

import com.allin.teaming.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByPhone(String Phone);
    boolean existsByEmail(String email);
}
