package com.testproj.db.auth;

import com.testproj.db.model.User;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AuthUserDS {
    protected final DSLContext jooq;
    public AuthUserDS(@Qualifier("authDslContext") DSLContext dsl) {this.jooq = dsl;}

    public List<User> list() {
        return null;
    }

    public User findByUsername(String username) {//todo
        return null;
    }
}