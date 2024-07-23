package com.allin.teaming.shedule.repository;

import com.allin.teaming.shedule.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByTitle(String title);
    List<Event> findByScheduleId(Long id);
}
