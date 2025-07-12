package com.api.server.persistence.repository.material;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import org.springframework.stereotype.Repository;

import com.api.server.persistence.entity.material.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    Optional<Material> findByCode(String code);

    Page<Material> findByName(String name, Pageable pageable);

    Page<Material> findByMaterialcategoryName(String materialcategoryname, Pageable pageable);

    Page<Material> findByMaterialcategoryNameAndStatus(String materialcategoryname, Material.MaterialStatus status, Pageable pageable);

    Page<Material> findByStatus(Material.MaterialStatus status, Pageable pageable);

    boolean existsByCode(String code);
}
