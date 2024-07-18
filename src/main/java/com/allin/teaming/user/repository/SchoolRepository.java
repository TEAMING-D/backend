package com.allin.teaming.user.repository;

import com.allin.teaming.user.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
}
