package com.ssg.assignment.dto.response;

import com.ssg.assignment.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewResponseDto {

    private Long totalCount;
    private float score;
    private Long cursor;
    private List<ReviewDto> reviews = new ArrayList<>();



    public static ReviewResponseDto of(Long totalCount, float score, List<Review> reviews) {
        return new ReviewResponseDto(
                totalCount,
                score,
                reviews.isEmpty() ? null : reviews.get(reviews.size() - 1).getId(),
                reviews.stream()
                        .map(ReviewDto::from)
                        .toList()
        );
    }

}
