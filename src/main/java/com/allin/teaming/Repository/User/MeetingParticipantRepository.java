package com.allin.teaming.Repository.User;

import com.allin.teaming.Domain.User.MeetingParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingParticipantRepository extends JpaRepository<MeetingParticipant, Long> {
}
