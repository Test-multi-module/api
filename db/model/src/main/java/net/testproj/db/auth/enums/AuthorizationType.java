package net.testproj.db.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthorizationType {
    GOOGLE(1, "google");

    private final Integer code;
    private final String registrationId;

    public static AuthorizationType fromRegistrationId(String id) {
        if (id == null) throw new IllegalArgumentException("provider id is null");

        for (AuthorizationType p : values()) {
            if (p.registrationId.equals(id)) return p;
        }
        throw new IllegalArgumentException("Unknown provider: " + id);
    }
}
