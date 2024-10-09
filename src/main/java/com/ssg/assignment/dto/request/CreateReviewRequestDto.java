package com.ssg.assignment.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class CreateReviewRequestDto {

    private Long userId;

    @Min(1)
    @Max(5) // 1~5 점수만 가능
    private int score;
    private String content;
}
