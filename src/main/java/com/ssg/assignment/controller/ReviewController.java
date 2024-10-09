package com.ssg.assignment.controller;

import com.ssg.assignment.dto.request.CreateReviewRequestDto;
import com.ssg.assignment.dto.response.ReviewResponseDto;
import com.ssg.assignment.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/products")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{productId}/reviews")
    public ResponseEntity<Void> addReview(@PathVariable Long productId,
                                          @RequestPart(value = "file", required = false)MultipartFile file,
                                          @RequestPart("review")CreateReviewRequestDto dto) {
        reviewService.addReview(productId, file, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{productId}/reviews")
    public ResponseEntity<ReviewResponseDto> getProductReviews(@PathVariable Long productId,
                                                               @RequestParam Long cursor,
                                                               @RequestParam(defaultValue = "10") int size) {
        ReviewResponseDto response = reviewService.getReviewList(productId, cursor, size);
        return ResponseEntity.ok(response);
    }

}

