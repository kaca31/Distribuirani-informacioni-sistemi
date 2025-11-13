package com.example.reviewservice.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    private Long id;
    private Long bookId;
    private Long userId;
    private Integer rating;
    private String comment;
    private LocalDateTime date;
}
