package com.allin.teaming.Archive.repository;

import com.allin.teaming.Archive.domain.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Long> {
}
