package com.testproj.api.dtos.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class RegisterRequestDTO {//obj will be mapped to 2 objects - AuthUser and User//todo documentation via swagger
    private String role;//todo via Enum
    private String login;
    private String password;
    private String email;

    private String nickName;
    private Date dayOfBirth;
}