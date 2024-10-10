package com.ssg.assignment.repository;

import com.ssg.assignment.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query(
            "UPDATE Product p " +
                    "SET p.score = CASE " +
                    "WHEN p.reviewCount = 0 THEN :score " +
                    "ELSE (p.score * p.reviewCount + :score) / (p.reviewCount + 1.0) END, " +
                    "p.reviewCount = p.reviewCount + 1 " +
                    "WHERE p.id = :productId"
    )
    void updateReviewCountAndScore(Long productId, float score);
}
