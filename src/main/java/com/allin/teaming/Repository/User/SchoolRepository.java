package com.allin.teaming.Repository.User;

import com.allin.teaming.Domain.User.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
}
