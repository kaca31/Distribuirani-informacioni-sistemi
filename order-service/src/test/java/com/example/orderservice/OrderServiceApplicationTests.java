package com.example.orderservice;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceApplicationTests {

    private OrderRepository repo;
    private RestTemplate rest;
    private OrderService service;

    @BeforeEach
    void setup() {
        repo = mock(OrderRepository.class);
        rest = mock(RestTemplate.class);
        service = new OrderService(repo, rest);
    }

    @Test
    void testCreateOrder_Success() {
        OrderRequest req = new OrderRequest(1L, 100L, 2);

        // Mock catalog-service response
        Map<String, Object> bookResponse = Map.of(
                "id", 1L,
                "title", "Effective Java",
                "author", "Joshua Bloch",
                "price", "45.50",
                "stockQuantity", 10
        );
        when(rest.getForObject("http://catalog-service:8081/api/books/1", Map.class))
                .thenReturn(bookResponse);

        // Mock repo save
        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        when(repo.save(captor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // Call service
        Order created = service.create(req);

        // Verify inventory checks
        verify(rest).postForLocation(eq("http://inventory-service:8084/api/inventory"),
                eq(Map.of("bookId", 1L, "quantityAvailable", 0)));
        verify(rest).put(eq("http://inventory-service:8084/api/inventory/1/decrease"),
                eq(Map.of("amount", 2)));

        // Verify order saved
        assertNotNull(created);
        assertEquals(1L, created.getBookId());
        assertEquals(100L, created.getUserId());
        assertEquals(2, created.getQuantity());
        assertEquals(new BigDecimal("91.00"), created.getTotalPrice());
        assertNotNull(created.getOrderDate());

        // Check that repo.save was called with correct data
        Order saved = captor.getValue();
        assertEquals(1L, saved.getBookId());
        assertEquals(2, saved.getQuantity());
    }

    @Test
    void testCreateOrder_BookNotFound() {
        OrderRequest req = new OrderRequest(2L, 200L, 1);

        when(rest.getForObject("http://catalog-service:8081/api/books/2", Map.class))
                .thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.create(req));
        assertEquals("Book not found", ex.getMessage());
    }
}
