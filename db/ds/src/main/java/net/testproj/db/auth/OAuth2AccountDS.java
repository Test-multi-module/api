package net.testproj.db.auth;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static net.testproj.db.auth.tables.Oauth2Accounts.OAUTH2_ACCOUNTS;


@Service
public class OAuth2AccountDS {

    protected final DSLContext jooq;
    public OAuth2AccountDS(DSLContext dsl) {this.jooq = dsl;}

    public List<OAuth2Account> list() {
        List<OAuth2Account> oAuth2Accounts = jooq.select().from(OAUTH2_ACCOUNTS).fetchInto(OAuth2Account.class);
        oAuth2Accounts.get(0).setProviderAvatarUrl("i am from ds service");
        return oAuth2Accounts;
    }

    public OAuth2Account findById(UUID prdId){
        return jooq.select().from(OAUTH2_ACCOUNTS).where(OAUTH2_ACCOUNTS.ID.eq(prdId)).fetchInto(OAuth2Account.class).get(0);
    }

}