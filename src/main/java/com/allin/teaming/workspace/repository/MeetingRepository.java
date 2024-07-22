package com.allin.teaming.workspace.repository;

import com.allin.teaming.workspace.domain.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}

