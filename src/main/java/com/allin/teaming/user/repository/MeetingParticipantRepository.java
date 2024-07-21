package com.allin.teaming.user.repository;

import com.allin.teaming.user.domain.MeetingParticipant;
import com.allin.teaming.workspace.domain.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingParticipantRepository extends JpaRepository<MeetingParticipant, Long> {
    List<MeetingParticipant> findByMeeting(Meeting meeting);
}
