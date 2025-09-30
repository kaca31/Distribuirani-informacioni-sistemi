package com.example.order_service.dto;

import java.time.LocalDateTime;

public class OrderDTO {
    private Long id;
    private Long bookId;
    private double price;
    private int quantity;
    private double totalPrice;
    private LocalDateTime orderDate;

    public OrderDTO() {}

    public OrderDTO(Long id, Long bookId, double price, int quantity, double totalPrice, LocalDateTime orderDate) {
        this.id = id;
        this.bookId = bookId;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    // getteri i setteri
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
}
