package com.allin.teaming.Repository.Schedule;

import com.allin.teaming.Domain.Schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
