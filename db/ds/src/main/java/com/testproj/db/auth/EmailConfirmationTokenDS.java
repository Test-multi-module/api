package com.testproj.db.auth;

import com.testproj.db.auth.schema.enums.ConfirmationTokenStatus;
import com.testproj.db.auth.schema.model.EmailConfirmationToken;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

import static com.testproj.db.auth.schema.Tables.EMAIL_CONFIRMATION_TOKENS;

@Service
public class EmailConfirmationTokenDS {
    protected final DSLContext jooq;
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    public EmailConfirmationTokenDS(@Qualifier("authDslContext") DSLContext dsl){ this.jooq = dsl;}

    public EmailConfirmationToken create(UUID userId) {
        EmailConfirmationToken emailConfirmationToken = EmailConfirmationToken.builder()
                .id(UUID.randomUUID())
                .token(UUID.randomUUID())
                .status(ConfirmationTokenStatus.PENDING)
                .user_id(userId)
                .build();

        setDates(emailConfirmationToken);
        jooq.insertInto(EMAIL_CONFIRMATION_TOKENS).set(jooq.newRecord(EMAIL_CONFIRMATION_TOKENS, emailConfirmationToken)).execute();

        return emailConfirmationToken;
    }

    private void setDates(EmailConfirmationToken emailConfirmationToken){//todo: re-evaluate, how I'm assigning dates
        Date now = new Date(System.currentTimeMillis());
        emailConfirmationToken.setCreated_at(now);
        emailConfirmationToken.setExpires_at(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
    }
}
