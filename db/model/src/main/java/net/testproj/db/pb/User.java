package net.testproj.db.pb;

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
public class User {
    private UUID id;
    private Date dayOfBirth;
    private String nickName;
}