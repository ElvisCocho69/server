package com.api.server.service.material;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.api.server.persistence.entity.material.Material;
import com.api.server.persistence.entity.material.MaterialCategory;
import com.api.server.persistence.entity.material.MaterialInventory;
import com.api.server.persistence.entity.material.MaterialMovement;
import com.api.server.persistence.entity.material.Supplier;

public interface MaterialService {

    // Métodos para materiales
    Page<Material> findAll(String materialCategoryName, String status, Pageable pageable);

    Material saveMaterial(Material material);

    Material updateMaterial(Long id, Material material);

    Optional<Material> getMaterialByCode(String code);

    Optional<Material> getMaterialById(Long id);

    // Métodos para movimientos
    Page<MaterialMovement> getOneMaterialMovements(String materialCode, Pageable pageable);

    MaterialMovement registerMovement(String materialCode, String movementType, Double quantity, String description, Long userId);

    Page<MaterialMovement> getAllMovements(String materialCategoryName, LocalDateTime startDate, LocalDateTime endDate, String searchTerm, Pageable pageable);

    // Métodos para inventarios
    Optional<MaterialInventory> getInventoryByMaterialCode(String materialCode);

    // Métodos para categorías de materiales
    Page<MaterialCategory> findAll(String status, Pageable pageable);
    
    MaterialCategory saveMaterialCategory(MaterialCategory materialCategory);

    MaterialCategory updateMaterialCategory(Long id, MaterialCategory materialCategory);

    Optional<MaterialCategory> getMaterialCategoryById(Long id);

    // Métodos para proveedores
    Page<Supplier> findAllSuppliers(String status, Pageable pageable);
    
    Supplier saveSupplier(Supplier supplier);

    Supplier updateSupplier(Long id, Supplier supplier);

    Optional<Supplier> getSupplierById(Long id);

}
