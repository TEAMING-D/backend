package com.allin.teaming.Repository.Schedule;

import com.allin.teaming.Domain.Schedule.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
