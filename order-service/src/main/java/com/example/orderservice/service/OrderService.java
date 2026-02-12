package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Service
public class OrderService {
    private final OrderRepository repo;
    private final RestTemplate rest;

    public OrderService(OrderRepository repo, RestTemplate rest) {
        this.repo = repo;
        this.rest = rest;
    }

   public Order create(OrderRequest r) {
    Map<String, Object> book = rest.getForObject(
            "http://catalog-service:8081/api/books/" + r.getBookId(), Map.class);
    if (book == null)
        throw new RuntimeException("Book not found");

    BigDecimal price = new BigDecimal(book.get("price").toString());
    BigDecimal total = price.multiply(BigDecimal.valueOf(r.getQuantity()));

    Map<String, Object> inventory = rest.getForObject(
            "http://inventory-service:8084/api/inventory/" + r.getBookId(), Map.class);

    if (inventory == null || (Integer) inventory.get("quantityAvailable") < r.getQuantity()) {
        throw new RuntimeException("Not enough stock in inventory");
    }

    Order o = Order.builder()
            .bookId(r.getBookId())
            .userId(r.getUserId())
            .quantity(r.getQuantity())
            .totalPrice(total)
            .orderDate(LocalDateTime.now())
            .build();

    Order savedOrder = repo.save(o);

    try {
        rest.put(
                "http://inventory-service:8084/api/inventory/" + r.getBookId() + "/decrease",
                Map.of("amount", r.getQuantity())
        );
    } catch (HttpClientErrorException e) {
        repo.delete(savedOrder);
        throw new RuntimeException("Failed to decrease inventory, order rolled back: "
                + e.getResponseBodyAsString());
    }

    rest.put(
            "http://catalog-service:8081/api/books/" + r.getBookId() + "/decreaseStock",
            Map.of("amount", r.getQuantity()));

    return savedOrder;
}


    public List<Order> getAll() {
        return repo.findAll();
    }

    public Order get(Long id) {
        return repo.findById(id).orElse(null);
    }

}
