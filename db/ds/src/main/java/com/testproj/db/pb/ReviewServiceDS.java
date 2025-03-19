package com.testproj.db.pb;


import com.testproj.db.model.Review;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.testproj.db.schema.generated.Tables.REVIEW;


@Service
public class ReviewServiceDS {
    protected final DSLContext jooq;
    public ReviewServiceDS(@Qualifier("publicDslContext") DSLContext dsl) {this.jooq = dsl;}

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
