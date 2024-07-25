package com.allin.teaming.shedule.repository;

import com.allin.teaming.shedule.domain.Event;
import com.allin.teaming.shedule.domain.Schedule;
import com.allin.teaming.shedule.domain.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByTitle(String title);
    List<Event> findByScheduleId(Long id);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM Event e " +
            "WHERE e.schedule = :schedule " +
            "AND e.week = :week " +
            "AND ((e.start_time < :endTime AND e.end_time > :startTime) " +
            "OR (e.start_time < :startTime AND e.end_time > :endTime) " +
            "OR (e.start_time >= :startTime AND e.end_time <= :endTime))")
    boolean existsByScheduleAndWeekAndTimeRange(
            @Param("schedule") Schedule schedule,
            @Param("week") Week week,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);
}
