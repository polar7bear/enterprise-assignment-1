package com.ssg.assignment.dto.response;

import com.ssg.assignment.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewDto {

    private Long id;
    private Long userId;
    private int score;
    private String content;
    private String imageUrl;
    private LocalDateTime dateTime;



    public static ReviewDto from(Review review) {
        return new ReviewDto(
                review.getId(),
                review.getUserId(),
                review.getReviewScore(),
                review.getContent(),
                review.getImageUrl(),
                review.getCreatedAt()
        );
    }
}
