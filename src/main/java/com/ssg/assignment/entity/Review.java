package com.ssg.assignment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "review", indexes = {
        @Index(name = "idx_product_createdAt_id", columnList = "product_id, createdAt, id")
})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int reviewScore;

    private String content;

    private String imageUrl;

    private Long userId;

    private LocalDateTime createdAt;

    public static Review of(Product product, int reviewScore, String content, String imageUrl, Long userId) {
        return Review.builder()
                .product(product)
                .reviewScore(reviewScore)
                .content(content)
                .imageUrl(imageUrl)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
