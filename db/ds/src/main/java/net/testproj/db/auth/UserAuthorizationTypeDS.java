package net.testproj.db.auth;

import com.github.f4b6a3.uuid.UuidCreator;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import static net.testproj.db.auth.tables.UserAuthorizationTypes.USER_AUTHORIZATION_TYPES;

@Service
public class UserAuthorizationTypeDS {
    protected final DSLContext jooq;
    public UserAuthorizationTypeDS(DSLContext dsl) {this.jooq = dsl;}

    public void insert(UserAuthorizationType obj){
        obj.setId(UuidCreator.getTimeOrderedEpoch());
        jooq.insertInto(USER_AUTHORIZATION_TYPES).set(jooq.newRecord(USER_AUTHORIZATION_TYPES, obj)).execute();
    }

}
