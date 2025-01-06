package com.db.ds;


import com.db.model.Product;
import com.db.model.Review;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.db.schema.generated.Tables.REVIEW;


@Service
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ReviewServiceDS {
    protected final DSLContext jooq;

    public List<Review> list() {
        return null;
    }

    public Review findById(int prdId){
        return null;
    }
    public void addReview(Review review){}

    public List<Review> getReviewsForProduct(int prdId){
        return jooq.select().from(REVIEW).where(REVIEW.PRODUCT_ID.eq(prdId)).fetchInto(Review.class);
    }
}
