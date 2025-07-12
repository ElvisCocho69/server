package com.api.server.persistence.repository.material;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.server.persistence.entity.material.Material;
import com.api.server.persistence.entity.material.MaterialInventory;

@Repository
public interface MaterialInventoryRepository extends JpaRepository<MaterialInventory, Long> {

    Optional<MaterialInventory> findByMaterialId(Long materialId);

    Optional<MaterialInventory> findByMaterialCode(String materialCode);

    Page<MaterialInventory> findByMaterialStatus(Material.MaterialStatus materialStatus, Pageable pageable);
    
}
