package net.testproj.db.pb;


import net.testproj.db.pb.schema.Tables;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReviewDS {
    protected final DSLContext jooq;
    public ReviewDS(@Qualifier("publicDslContext") DSLContext dsl) {this.jooq = dsl;}

    public List<Review> list() {
        return null;
    }

    public Review findById(int prdId){
        return null;
    }
    public void addReview(Review review){}

    public List<Review> getReviewsForProduct(int prdId){
        return jooq.select().from(Tables.REVIEW).where(Tables.REVIEW.PRODUCT_ID.eq(prdId)).fetchInto(Review.class);
    }
}
