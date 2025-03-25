package com.testproj.api.services;

import com.testproj.api.dtos.models.ReviewDTO;
import com.testproj.db.pb.schema.model.Review;
import com.testproj.db.pb.ReviewDS;
import io.beanmapper.BeanMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class ReviewService {
    private final BeanMapper beanMapper;
    private final ReviewDS reviewDS;//TODO final or Autowire
    public List<ReviewDTO> list() {
        return beanMapper.map(reviewDS.list(), ReviewDTO.class);
    }

    public ReviewDTO findById(Integer prdId){
        return beanMapper.map(reviewDS.findById(prdId), ReviewDTO.class);
    }

    public void addReview(Review review){
        reviewDS.addReview(review);
    }

    public List<ReviewDTO> getReviewsForProduct(Integer prdId) {
        return beanMapper.map(reviewDS.getReviewsForProduct(prdId), ReviewDTO.class);
    }
}