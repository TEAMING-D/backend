package com.allin.teaming.user.repository;

import com.allin.teaming.user.domain.MeetingParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingParticipantRepository extends JpaRepository<MeetingParticipant, Long> {
}
