package com.allin.teaming.workspace.repository;

import com.allin.teaming.workspace.domain.Work;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkRepository extends JpaRepository<Work, Long> {
}