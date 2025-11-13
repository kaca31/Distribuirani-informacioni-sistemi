package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderService {
    private final OrderRepository repo;
    private final RestTemplate rest;

    public OrderService(OrderRepository repo, RestTemplate rest) {
        this.repo = repo;
        this.rest = rest;
    }

    public Order create(OrderRequest r) {
        var book = rest.getForObject("http://catalog-service:8081/api/books/" + r.getBookId(), java.util.Map.class);
        if (book == null)
            throw new RuntimeException("Book not found");
        BigDecimal price = new BigDecimal(book.get("price").toString());
        BigDecimal total = price.multiply(BigDecimal.valueOf(r.getQuantity()));
        // ensure inventory exists (create if needed)
        rest.postForLocation("http://inventory-service:8084/api/inventory",
                java.util.Map.of("bookId", r.getBookId(), "quantityAvailable", 0));
        // decrease inventory
        rest.put("http://inventory-service:8084/api/inventory/" + r.getBookId() + "/decrease",
                java.util.Map.of("amount", r.getQuantity()));
        Order o = Order.builder().bookId(r.getBookId()).userId(r.getUserId()).quantity(r.getQuantity())
                .totalPrice(total).orderDate(LocalDateTime.now()).build();
        return repo.save(o);
    }
}
