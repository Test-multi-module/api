package net.testproj.db.api;


import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import java.util.UUID;

import static net.testproj.db.api.tables.Users.USERS;


@Service
public class ApiUserDS {

    protected final DSLContext jooq;
    public ApiUserDS(DSLContext dsl) {this.jooq = dsl;}

    public User getById(UUID id){//todo entityNotFound???? what if nothing was found?
        return jooq.select().from(USERS).where(USERS.ID.eq(id)).fetchOneInto(User.class);
    }

    public User insert(User obj){
        return jooq.insertInto(USERS)
                .set(jooq.newRecord(USERS, obj))
                .returning()
                .fetchOneInto(User.class);
    }
}