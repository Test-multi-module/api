package net.testproj.db.auth.schema.model;

import net.testproj.db.auth.schema.enums.ConfirmationTokenStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Builder
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailConfirmationToken {

    private UUID id;

    private Date created_at;

    private Date expires_at;

    private ConfirmationTokenStatus status;

    private UUID token;

    private UUID user_id;
}