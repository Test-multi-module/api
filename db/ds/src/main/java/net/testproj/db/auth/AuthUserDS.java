package net.testproj.db.auth;

import net.testproj.db.pb.User;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static net.testproj.db.auth.tables.Users.USERS;


@Service
public class AuthUserDS {
    protected final DSLContext jooq;
    public AuthUserDS(DSLContext dsl) {this.jooq = dsl;}

    public List<User> list() {
        return null;
    }

    public AuthUser create(AuthUser user){
        Date now = new Date();
        user.setUpdatedAt(Instant.now());
        user.setCreatedAt(Instant.now());
        user.setId(UUID.randomUUID());
        user.setDisabled(false);//todo analize if it really should be set here. mb shoud be dropped
        user.setDeleted(false);//todo analize if it really should be set here. mb shoud be dropped
        user.setAuthorizationType(0);//todo | enum??//0 by default now

        jooq.insertInto(USERS).set(jooq.newRecord(USERS, user)).execute();//todo uncomment
        return user;
    }
}