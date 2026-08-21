package net.testproj.db.auth;

import com.github.f4b6a3.uuid.UuidCreator;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static net.testproj.db.auth.tables.AuthUsers.AUTH_USERS;


@Service
public class AuthUserDS {

    protected final DSLContext jooq;

    public AuthUserDS(DSLContext dsl) {this.jooq = dsl;}

    public AuthUser insert(AuthUser obj){
        UUID id = UuidCreator.getTimeOrderedEpoch();
        obj.setId(id);
        jooq.insertInto(AUTH_USERS).set(jooq.newRecord(AUTH_USERS, obj)).execute();
        return jooq.select().from(AUTH_USERS).where(AUTH_USERS.ID.eq(id)).fetchInto(AuthUser.class).getFirst();
    }

    public void update(AuthUser obj){
        jooq.update(AUTH_USERS)
                .set(jooq.newRecord(AUTH_USERS, obj))
                .where(AUTH_USERS.ID.eq(obj.getId()))
                .returning()
                .fetchOneInto(AuthUser.class);
    }

    public Boolean isProfileCompleted(UUID userId) {
       return jooq.select().from(AUTH_USERS).where(AUTH_USERS.ID.eq(userId))
               .fetchInto(AuthUser.class).getFirst()
               .getProfileCompleted();
    }

    public AuthUser getById(UUID id){//todo entityNotFound???? what if nothing was found?
        return jooq.select().from(AUTH_USERS).where(AUTH_USERS.ID.eq(id)).fetchOneInto(AuthUser.class);
    }
}