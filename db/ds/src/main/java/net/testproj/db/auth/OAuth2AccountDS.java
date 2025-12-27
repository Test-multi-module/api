package net.testproj.db.auth;

import com.github.f4b6a3.uuid.UuidCreator;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import static net.testproj.db.auth.tables.Oauth2Accounts.OAUTH2_ACCOUNTS;


@Service
public class OAuth2AccountDS {

    protected final DSLContext jooq;
    public OAuth2AccountDS(DSLContext dsl) {this.jooq = dsl;}

    public OAuth2Account getByProviderAndProviderUserId(String providerUserId, String provider){
        return jooq.select().from(OAUTH2_ACCOUNTS)
                .where(OAUTH2_ACCOUNTS.PROVIDER_USER_ID.eq(providerUserId)
                        .and(OAUTH2_ACCOUNTS.PROVIDER.eq(provider)))
                .fetchInto(OAuth2Account.class).getFirst();
    }

    public void insert(OAuth2Account obj){
        obj.setId(UuidCreator.getTimeOrderedEpoch());
        jooq.insertInto(OAUTH2_ACCOUNTS).set(jooq.newRecord(OAUTH2_ACCOUNTS, obj)).execute();
    }

}