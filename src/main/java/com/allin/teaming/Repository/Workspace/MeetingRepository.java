package com.allin.teaming.Repository.Workspace;

import com.allin.teaming.Domain.Workspace.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
