package com.example.order_service.controller;

import com.example.order_service.dto.OrderDTO;
import com.example.order_service.model.Order;
import com.example.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    // Mapiranje Order -> OrderDTO
    private OrderDTO mapToDTO(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getBookId(),
                order.getPrice(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getOrderDate()
        );
    }

    // Mapiranje OrderDTO -> Order
    private Order mapToEntity(OrderDTO dto) {
        return new Order(
                dto.getId(),
                dto.getBookId(),
                dto.getPrice(),
                dto.getQuantity()
        );
    }

    // GET /orders
    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // GET /orders/{id}
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(value -> ResponseEntity.ok(mapToDTO(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /orders
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        Order savedOrder = orderRepository.save(mapToEntity(orderDTO));
        return ResponseEntity.ok(mapToDTO(savedOrder));
    }

    // PUT /orders/{id}
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        Optional<Order> existingOrder = orderRepository.findById(id);
        if (!existingOrder.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        orderDTO.setId(id);
        Order updatedOrder = orderRepository.save(mapToEntity(orderDTO));
        return ResponseEntity.ok(mapToDTO(updatedOrder));
    }

    // DELETE /orders/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        orderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
