package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.InventoryDto;
import com.example.inventoryservice.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<InventoryDto> get(@PathVariable Long bookId) {
        var i = service.get(bookId);
        if (i == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(i);
    }

    @PostMapping
    public ResponseEntity<InventoryDto> create(@RequestBody InventoryDto dto) {
        return ResponseEntity.ok(service.createOrUpdate(dto));
    }

    @PutMapping("/{bookId}/decrease")
    public ResponseEntity<?> decrease(@PathVariable Long bookId,
            @RequestBody(required = false) Map<String, Integer> body) {
        int amt = body == null || !body.containsKey("amount") ? 1 : body.get("amount");
        try {
            return ResponseEntity.ok(service.decrease(bookId, amt));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }
}
