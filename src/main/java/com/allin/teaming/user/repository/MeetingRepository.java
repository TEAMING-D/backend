package com.allin.teaming.user.repository;

import com.allin.teaming.user.domain.User;
import com.allin.teaming.workspace.domain.Meeting;
import com.allin.teaming.workspace.domain.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByWorkspace(Workspace workspace);
    List<Meeting> findByUser(User user);
}
