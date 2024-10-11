package com.ssg.assignment.service;

import com.ssg.assignment.dto.request.CreateReviewRequestDto;
import com.ssg.assignment.dto.response.ReviewResponseDto;
import com.ssg.assignment.entity.Product;
import com.ssg.assignment.entity.Review;
import com.ssg.assignment.repository.ProductRepository;
import com.ssg.assignment.repository.ReviewRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final S3Service s3Service;


    @Retryable(
            value = {OptimisticLockException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    @Transactional
    public void addReview(Long productId, MultipartFile file, CreateReviewRequestDto dto) {
        try {
            Product product = getProduct(productId);
            checkDuplicateReview(productId, dto);

            String imageUrl = file != null && !file.isEmpty() ? s3Service.uploadFile(file) : null;

            Review review = Review.of(product,
                    dto.getScore(),
                    dto.getContent(),
                    imageUrl,
                    dto.getUserId());
            reviewRepository.save(review);
            updateProductReviewStatsByQuery(productId, dto.getScore());
        } catch (OptimisticLockException e) {
            throw new RuntimeException("리뷰등록에 실패하였습니다. 다시 시도해주세요.");
        }
    }

    @Transactional(readOnly = true)
    public ReviewResponseDto getReviewList(Long productId, Long cursor, int size) {
        Product product = getProduct(productId);

        Pageable pageable = PageRequest.of(0, size);
        List<Review> reviews = reviewRepository.findReviewsByProductId(productId, cursor, pageable);

        return ReviewResponseDto.of(product.getReviewCount(), product.getScore(), reviews);
    }

    private void checkDuplicateReview(Long productId, CreateReviewRequestDto dto) {
        reviewRepository.findByUserIdAndProductId(dto.getUserId(), productId)
                .ifPresent(r -> {
                    throw new RuntimeException("아마 해당 상품에 리뷰를 작성하셨습니다.");
                });
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품입니다."));
    }

    /*private void updateProductReviewStats(Product product) {
        List<Review> reviews = reviewRepository.findByProductId(product.getId());
        long reviewCount = reviews.size();
        double averageScore = reviews.stream()
                .mapToDouble(Review::getReviewScore)
                .average()
                .orElse(0.0);

        product.setReviewCount(reviewCount);
        product.setScore((float) averageScore);
        productRepository.save(product);
    }*/

    // 동시성 문제를 방지하기 위해 상품의 리뷰 수와 리뷰 점수를 원자적으로 업데이트하는 쿼리를 실행
    private void updateProductReviewStatsByQuery(Long productId, int score) {
        productRepository.updateReviewCountAndScore(productId, score);
    }

}
