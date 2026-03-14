package net.testproj.api.DTOs;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
//todo скорее всего перейду на использование аннотации @Data которая обьединяет геттеры сеттеры toString и т.д.
public class UserDTO {
    private UUID id;
    private Date dayOfBirth;
    private String nickName;
}
