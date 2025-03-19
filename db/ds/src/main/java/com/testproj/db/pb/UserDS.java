package com.testproj.db.pb;

import com.testproj.db.model.User;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.testproj.db.schema.generated.Tables.REVIEW;

@Service
public class UserDS {
    protected final DSLContext jooq;
    public UserDS(@Qualifier("publicDslContext") DSLContext dsl) {this.jooq = dsl;}

    public List<User> list() {
        return null;
    }

    public User findById(int prdId){
        return null;
    }
    public void addReview(User review){}

    public User findByUsername(String username) {//todo
        return null;
    }

    public List<User> getReviewsForProduct(int prdId){
        return jooq.select().from(REVIEW).where(REVIEW.PRODUCT_ID.eq(prdId)).fetchInto(User.class);
    }
}