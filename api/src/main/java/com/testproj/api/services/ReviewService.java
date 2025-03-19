package com.testproj.api.services;

import com.testproj.api.dtos.models.ReviewDTO;
import com.testproj.db.ReviewServiceDS;
import com.testproj.db.model.Review;
import io.beanmapper.BeanMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class ReviewService {
    private final BeanMapper beanMapper;
    private final ReviewServiceDS reviewServiceDS;//TODO final or Autowire
    public List<ReviewDTO> list() {
        return beanMapper.map(reviewServiceDS.list(), ReviewDTO.class);
    }

    public ReviewDTO findById(Integer prdId){
        return beanMapper.map(reviewServiceDS.findById(prdId), ReviewDTO.class);
    }

    public void addReview(Review review){
        reviewServiceDS.addReview(review);
    }

    public List<ReviewDTO> getReviewsForProduct(Integer prdId) {
        return beanMapper.map(reviewServiceDS.getReviewsForProduct(prdId), ReviewDTO.class);
    }
}