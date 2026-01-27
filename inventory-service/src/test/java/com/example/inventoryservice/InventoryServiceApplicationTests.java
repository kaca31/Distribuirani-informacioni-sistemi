package com.example.inventoryservice;

import org.junit.jupiter.api.Test;

import com.example.inventoryservice.dto.InventoryDto;
import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import com.example.inventoryservice.service.InventoryService;

import org.junit.jupiter.api.BeforeEach;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceApplicationTests {

    private InventoryRepository repo;
    private InventoryService service;

    @BeforeEach
    void setup() {
        repo = mock(InventoryRepository.class);
        service = new InventoryService(repo);
    }

    @Test
    void testGetInventory_Found() {
        Inventory inv = Inventory.builder()
                .bookId(101L)
                .quantityAvailable(50)
                .build();

        when(repo.findById(101L)).thenReturn(Optional.of(inv));

        InventoryDto dto = service.get(101L);

        assertNotNull(dto);
        assertEquals(101L, dto.getBookId());
        assertEquals(50, dto.getQuantityAvailable());
    }

    @Test
    void testGetInventory_NotFound() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        InventoryDto dto = service.get(999L);

        assertNull(dto);
    }

    @Test
    void testCreateOrUpdate_NewInventory() {
        InventoryDto dto = new InventoryDto(202L, 30);
        Inventory saved = Inventory.builder().bookId(202L).quantityAvailable(30).build();

        when(repo.save(any(Inventory.class))).thenReturn(saved);

        InventoryDto result = service.createOrUpdate(dto);

        assertNotNull(result);
        assertEquals(202L, result.getBookId());
        assertEquals(30, result.getQuantityAvailable());
        verify(repo, times(1)).save(any(Inventory.class));
    }

    @Test
    void testDecreaseInventory_Success() {
        Inventory inv = Inventory.builder()
                .bookId(303L)
                .quantityAvailable(20)
                .build();

        when(repo.findById(303L)).thenReturn(Optional.of(inv));
        when(repo.save(inv)).thenReturn(inv);

        InventoryDto updated = service.decrease(303L, 5);

        assertEquals(303L, updated.getBookId());
        assertEquals(15, updated.getQuantityAvailable());
    }

    @Test
    void testDecreaseInventory_NotEnoughStock() {
        Inventory inv = Inventory.builder()
                .bookId(404L)
                .quantityAvailable(3)
                .build();

        when(repo.findById(404L)).thenReturn(Optional.of(inv));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.decrease(404L, 5));
        assertEquals("Not enough stock", ex.getMessage());
    }

    @Test
    void testDecreaseInventory_NotFound() {
        when(repo.findById(505L)).thenReturn(Optional.empty());

        InventoryDto dto = service.decrease(505L, 1);
        assertNull(dto);
    }
}