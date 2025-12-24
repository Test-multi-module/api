package net.testproj.db.pb;


import org.jooq.DSLContext;
import org.springframework.stereotype.Service;


@Service
public class UserDS {

    protected final DSLContext jooq;
    public UserDS(DSLContext dsl) {this.jooq = dsl;}
}