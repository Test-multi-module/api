package com.testproj.api.controllers;

import com.testproj.api.dtos.models.ReviewDTO;
import com.testproj.api.services.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RestControllerAdvice
@RequestMapping("api/private/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }
    @GetMapping("/{product-id}")
    public List<ReviewDTO> getReviewsForProduct(@PathVariable("product-id") Integer prdId) {
        return reviewService.getReviewsForProduct(prdId);
    }
}