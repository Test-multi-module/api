package net.testproj.db.pb;


import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class UserDS {

    protected final DSLContext jooq;
    public UserDS(@Qualifier("publicDslContext") DSLContext dsl) {this.jooq = dsl;}

    public void delete(int id){}
}