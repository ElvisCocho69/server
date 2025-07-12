package com.api.server.persistence.repository.structure;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.server.persistence.entity.structure.Structure;

public interface StructureRepository extends JpaRepository<Structure, Long> {

    Optional<Structure> findByName(String name);

    Page<Structure> findByOrderdetailOrderId(Long orderId, Pageable pageable);
    
}
