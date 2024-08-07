package com.allin.teaming.shedule.repository;

import com.allin.teaming.shedule.domain.Schedule;
import com.allin.teaming.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByUser(User user);
}
