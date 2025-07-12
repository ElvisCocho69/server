package com.api.server.persistence.repository.structure;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.api.server.persistence.entity.structure.Design;

@Repository
public interface DesignRepository extends JpaRepository<Design, Long> {

    Optional<Design> findByName(String name);

    Page<Design> findByStructureId(Long structureId, Pageable pageable);

}
