package com.allin.teaming.shedule.repository;

import com.allin.teaming.shedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
