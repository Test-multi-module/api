package net.testproj.api.DTOs;



import lombok.Getter;

import java.util.Date;

@Getter
public class CreateProfileRequestDTO {
    private Date dayOfBirth;
    private String nickName;
}
