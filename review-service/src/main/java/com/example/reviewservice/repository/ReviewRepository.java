package com.example.reviewservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.reviewservice.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
