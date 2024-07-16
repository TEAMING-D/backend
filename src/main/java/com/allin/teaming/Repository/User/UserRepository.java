package com.allin.teaming.Repository.User;

import com.allin.teaming.Domain.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByPhone(String Phone);
    boolean existsByEmail(String email);
}
