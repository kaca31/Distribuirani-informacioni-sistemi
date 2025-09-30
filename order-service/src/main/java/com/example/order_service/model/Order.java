package com.example.order_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookId;

    private double price;   // cena jedne knjige

    private int quantity;

    private double totalPrice;

    private LocalDateTime orderDate;

    public Order() {}

    public Order(Long id, Long bookId, double price, int quantity) {
        this.id = id;
        this.bookId = bookId;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    @PrePersist
    @PreUpdate
    private void prePersistOrUpdate() {
        this.totalPrice = this.price * this.quantity;
        if (this.orderDate == null) {
            this.orderDate = LocalDateTime.now();
        }
    }
}
