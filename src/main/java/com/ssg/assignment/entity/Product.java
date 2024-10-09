package com.ssg.assignment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long reviewCount;
    private float score;

    @OneToMany(mappedBy = "product")
    List<Review> reivews = new ArrayList<>();

    public Product(int i, float v) {
        this.reviewCount = i;
        this.score = v;
    }

    public Product() {
    }
}
