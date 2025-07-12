package com.api.server.persistence.repository.structure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.server.persistence.entity.structure.Milestone;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    Page<Milestone> findAllByStructureId(Long id, Pageable pageable);
}
