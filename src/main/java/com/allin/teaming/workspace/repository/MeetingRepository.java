package com.allin.teaming.workspace.repository;

import com.allin.teaming.workspace.domain.Meeting;
import com.allin.teaming.workspace.domain.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    Optional<Meeting> findByTitle(String title);
    boolean existsByTitle(String title);
    List<Meeting> findByWorkspace(Workspace workspace);
}

