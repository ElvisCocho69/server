package com.api.server.persistence.repository.material;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.server.persistence.entity.material.MaterialCategory;

@Repository
public interface MaterialCategoryRepository extends JpaRepository<MaterialCategory, Long> {

    Optional<MaterialCategory> findByName(String name);

    Page<MaterialCategory> findByStatus(MaterialCategory.CategoryStatus status, Pageable pageable);
    
}
