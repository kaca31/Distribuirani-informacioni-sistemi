package com.example.inventoryservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryDto {
    private Long bookId;
    private Integer quantityAvailable;
}
