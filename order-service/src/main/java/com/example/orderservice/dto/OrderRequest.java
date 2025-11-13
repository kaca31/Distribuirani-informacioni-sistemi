package com.example.orderservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    private Long bookId;
    private Long userId;
    private Integer quantity;
}
