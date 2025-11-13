package com.example.reviewservice.service;

import com.example.reviewservice.dto.ReviewDto;
import com.example.reviewservice.model.Review;
import com.example.reviewservice.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository repo;

    public ReviewService(ReviewRepository repo) {
        this.repo = repo;
    }

    public ReviewDto create(ReviewDto dto) {
        Review r = Review.builder().bookId(dto.getBookId()).userId(dto.getUserId()).rating(dto.getRating())
                .comment(dto.getComment()).date(LocalDateTime.now()).build();
        r = repo.save(r);
        return new ReviewDto(r.getId(), r.getBookId(), r.getUserId(), r.getRating(), r.getComment(), r.getDate());
    }

    public List<ReviewDto> getAll() {
        return repo.findAll().stream().map(
                r -> new ReviewDto(r.getId(), r.getBookId(), r.getUserId(), r.getRating(), r.getComment(), r.getDate()))
                .collect(Collectors.toList());
    }
}
