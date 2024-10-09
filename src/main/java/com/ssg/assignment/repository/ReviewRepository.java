package com.ssg.assignment.repository;

import com.ssg.assignment.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductId(Long id);

    Optional<Review> findByUserIdAndProductId(Long userId, Long ProductId);
}
