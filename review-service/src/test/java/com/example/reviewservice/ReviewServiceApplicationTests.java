package com.example.reviewservice;


import com.example.reviewservice.dto.ReviewDto;
import com.example.reviewservice.model.Review;
import com.example.reviewservice.repository.ReviewRepository;
import com.example.reviewservice.service.ReviewService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ReviewServiceApplicationTests {

    private ReviewRepository repo;
    private ReviewService service;

    @BeforeEach
    void setup() {
        repo = mock(ReviewRepository.class);
        service = new ReviewService(repo);
    }

    @Test
    void testCreateReview() {
        ReviewDto dto = new ReviewDto(null, 1L, 100L, 5, "Odlična knjiga!", null);

        // Mock repository save
        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        when(repo.save(captor.capture())).thenAnswer(invocation -> {
            Review r = invocation.getArgument(0);
            r.setId(1L); // simulate DB generated ID
            return r;
        });

        ReviewDto created = service.create(dto);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals(1L, created.getBookId());
        assertEquals(100L, created.getUserId());
        assertEquals(5, created.getRating());
        assertEquals("Odlična knjiga!", created.getComment());
        assertNotNull(created.getDate());

        // Check that repository save was called
        Review saved = captor.getValue();
        assertEquals("Odlična knjiga!", saved.getComment());
    }

    @Test
    void testGetAllReviews() {
        Review r1 = Review.builder().id(1L).bookId(1L).userId(100L).rating(5).comment("Sjajno!").date(LocalDateTime.now()).build();
        Review r2 = Review.builder().id(2L).bookId(2L).userId(101L).rating(4).comment("Dobro štivo").date(LocalDateTime.now()).build();

        when(repo.findAll()).thenReturn(Arrays.asList(r1, r2));

        List<ReviewDto> reviews = service.getAll();

        assertEquals(2, reviews.size());
        assertEquals("Sjajno!", reviews.get(0).getComment());
        assertEquals("Dobro štivo", reviews.get(1).getComment());
    }
}