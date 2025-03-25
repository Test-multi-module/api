package com.testproj.db.auth.schema.model;

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
public class AuthUser {

    private UUID id;
    private Date created;
    private Date updated;

    private boolean disabled;

    private String login;
    private String role;
    private String nickName;
    private String password;
    private String email;
}
