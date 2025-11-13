package com.example.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.inventoryservice.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
