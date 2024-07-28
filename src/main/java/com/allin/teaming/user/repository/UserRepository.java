package com.allin.teaming.user.repository;

import com.allin.teaming.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByUsername(String username);

    Optional<User> findBySchoolNum(String schoolNum);
    boolean existsByPhone(String Phone);
    boolean existsByEmail(String email);

    boolean existsByUsernameAndSchoolNum(String username, String schoolNum);
}
