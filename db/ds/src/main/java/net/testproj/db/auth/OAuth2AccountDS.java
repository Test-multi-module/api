package net.testproj.db.auth;

import com.github.f4b6a3.uuid.UuidCreator;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static net.testproj.db.auth.tables.Oauth2Accounts.OAUTH2_ACCOUNTS;


@Service
public class OAuth2AccountDS {

    protected final DSLContext jooq;
    public OAuth2AccountDS(DSLContext dsl) {this.jooq = dsl;}

    public OAuth2Account getByProviderUserIdAndProvider(String providerUserId, String provider){
        List<OAuth2Account> lst =  jooq.select().from(OAUTH2_ACCOUNTS)
                .where(OAUTH2_ACCOUNTS.PROVIDER_USER_ID.eq(providerUserId)
                        .and(OAUTH2_ACCOUNTS.PROVIDER.eq(provider)))
                .fetchInto(OAuth2Account.class);
        return lst.isEmpty() ? null : lst.getFirst();
    }

    public void insert(OAuth2Account obj){
        obj.setId(UuidCreator.getTimeOrderedEpoch());
        jooq.insertInto(OAUTH2_ACCOUNTS).set(jooq.newRecord(OAUTH2_ACCOUNTS, obj)).execute();
    }

    public void update(UUID id, String email, String givenName, String familyName){

        jooq.update(OAUTH2_ACCOUNTS)
                .set(OAUTH2_ACCOUNTS.EMAIL_AT_PROVIDER, email)
                .set(OAUTH2_ACCOUNTS.GIVEN_NAME, givenName)
                .set(OAUTH2_ACCOUNTS.FAMILY_NAME, familyName)
                .where(OAUTH2_ACCOUNTS.ID.eq(id)).execute();

    }
}

//todo анализ того как я юзаю методы jooq -вообще не воспринимать текущий код по работе с jooq  как качественный - нужно выработать подход-стиль
//todo как генерить дату корректно
//todo как на работке сделать table перпеменну, может даже AbstractDS-lalala