package com.api.server.persistence.repository.material;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.server.persistence.entity.material.Material;
import com.api.server.persistence.entity.material.MaterialMovement;

@Repository
public interface MaterialMovementRepository extends JpaRepository<MaterialMovement, Long> {
    
    Page<MaterialMovement> findByMaterialId(Long materialId, Pageable pageable);

    Page<MaterialMovement> findByMaterialCode(String materialCode, Pageable pageable);

    Page<MaterialMovement> findByMaterialStatus(Material.MaterialStatus materialStatus, Pageable pageable);

    @Query("SELECT mm FROM MaterialMovement mm WHERE mm.movementdate >= :startDate AND mm.movementdate <= :endDate")
    Page<MaterialMovement> findByMovementdateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

    @Query("SELECT mm FROM MaterialMovement mm WHERE mm.material.materialcategory.name = :materialCategoryName AND mm.movementdate >= :startDate AND mm.movementdate <= :endDate")
    Page<MaterialMovement> findByMaterialMaterialcategoryNameAndMovementdateBetween(
        @Param("materialCategoryName") String materialCategoryName,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );

    Page<MaterialMovement> findByMaterialMaterialcategoryName(String materialCategoryName, Pageable pageable);

    @Query("SELECT mm FROM MaterialMovement mm WHERE (:materialCategoryName IS NULL OR mm.material.materialcategory.name = :materialCategoryName) AND (:startDate IS NULL OR mm.movementdate >= :startDate) AND (:endDate IS NULL OR mm.movementdate <= :endDate) AND (:searchTerm IS NULL OR LOWER(mm.material.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<MaterialMovement> searchAllMovements(
        @Param("materialCategoryName") String materialCategoryName,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("searchTerm") String searchTerm,
        Pageable pageable
    );
}
