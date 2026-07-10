package net.testproj.db.auth;

import com.github.f4b6a3.uuid.UuidCreator;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static net.testproj.db.auth.tables.Users.USERS;


@Service
public class AuthUserDS {

    protected final DSLContext jooq;

    public AuthUserDS(DSLContext dsl) {this.jooq = dsl;}

    public User insert(User obj){
        UUID id = UuidCreator.getTimeOrderedEpoch();
        obj.setId(id);
        jooq.insertInto(USERS).set(jooq.newRecord(USERS, obj)).execute();
        return jooq.select().from(USERS).where(USERS.ID.eq(id)).fetchInto(User.class).getFirst();
    }

    public boolean isProfileCompleted(UUID userId) {
       return jooq.select().from(USERS).where(USERS.ID.eq(userId))
               .fetchInto(User.class).getFirst()
               .isProfileCompleted();
    }
}