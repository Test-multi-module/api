package net.testproj.db.api;


import org.jooq.DSLContext;
import org.springframework.stereotype.Service;


@Service
public class ApiUserDS {

    protected final DSLContext jooq;
    public ApiUserDS(DSLContext dsl) {this.jooq = dsl;}
}