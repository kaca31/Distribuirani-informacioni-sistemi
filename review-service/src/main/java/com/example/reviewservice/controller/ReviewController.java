package com.example.reviewservice.controller;

import com.example.reviewservice.dto.ReviewDto;
import com.example.reviewservice.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<ReviewDto> create(@RequestBody ReviewDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }
}
