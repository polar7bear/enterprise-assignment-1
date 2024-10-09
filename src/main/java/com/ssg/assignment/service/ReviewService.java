package com.ssg.assignment.service;

import com.ssg.assignment.dto.request.CreateReviewRequestDto;
import com.ssg.assignment.entity.Product;
import com.ssg.assignment.entity.Review;
import com.ssg.assignment.repository.ProductRepository;
import com.ssg.assignment.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final S3Service s3Service;

    @Transactional
    public void addReview(Long productId, MultipartFile file, CreateReviewRequestDto dto) {
        Product product = getProduct(productId);
        checkDuplicateReview(productId, dto); // 1명의 회원은 한 상품에 대해 한 개의 리뷰만 달 수 있음.

        String imageUrl = file != null && !file.isEmpty() ? s3Service.uploadFile(file) : null;

        Review review = Review.of(product,
                dto.getScore(),
                dto.getContent(),
                imageUrl,
                dto.getUserId());
        reviewRepository.save(review);
        updateProductReviewStats(product);

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

    private void updateProductReviewStats(Product product) {
        List<Review> reviews = reviewRepository.findByProductId(product.getId());
        long reviewCount = reviews.size();
        double averageScore = reviews.stream()
                .mapToDouble(Review::getReviewScore)
                .average()
                .orElse(0.0);

        product.setReviewCount(reviewCount);
        product.setScore((float) averageScore);
        productRepository.save(product);
    }


}
