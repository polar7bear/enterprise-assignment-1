package com.ssg.assignment.repository;

import com.ssg.assignment.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductId(Long id);

    Optional<Review> findByUserIdAndProductId(Long userId, Long ProductId);

    @Query(
            "SELECT r " +
                    "FROM Review r " +
                    "WHERE r.product.id = :productId " +
                    "AND (COALESCE(:cursor, 0) = 0 OR r.id < :cursor) " +
                    "ORDER BY r.createdAt DESC"
    )
    List<Review> findReviewsByProductId(Long productId, Long cursor, Pageable pageable);
}
