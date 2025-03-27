package com.testproj.db.auth;

import com.testproj.db.auth.schema.model.AuthUser;
import com.testproj.db.pb.schema.model.User;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.testproj.db.auth.schema.Tables.AUTH_USERS;


@Service
public class AuthUserDS {
    protected final DSLContext jooq;
    public AuthUserDS(@Qualifier("authDslContext") DSLContext dsl) {this.jooq = dsl;}

    public List<User> list() {
        return null;
    }

    public AuthUser findByUsername(String username) {//todo
        return null;
    }

    public AuthUser create(AuthUser user){
        Date now = new Date();
        user.setUpdated(now);
        user.setCreated(now);
        user.setId(UUID.randomUUID());
        user.setDisabled(false);//todo analize if it really should be set here. mb shoud be dropped
        user.setActivated(false);//todo analize if it really should be set here. mb shoud be dropped
        user.setRole(0);//todo | enum??

        jooq.insertInto(AUTH_USERS).set(jooq.newRecord(AUTH_USERS, user)).execute();
        return user;
    }
}