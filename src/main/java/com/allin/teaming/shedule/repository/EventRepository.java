package com.allin.teaming.shedule.repository;

import com.allin.teaming.shedule.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByTitle(String title);
}
