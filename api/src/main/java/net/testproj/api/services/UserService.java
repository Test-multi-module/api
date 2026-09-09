package net.testproj.api.services;

import io.beanmapper.BeanMapper;
import net.testproj.api.DTOs.CreateProfileRequestDTO;
import net.testproj.api.DTOs.UserDTO;
import net.testproj.db.api.ApiUserDS;
import lombok.AllArgsConstructor;
import net.testproj.db.api.ApiUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;
//todo: не задание - просто регламент: не испльзовать в апиМодуле auth-db-models and auth-db-ds

@Service
@AllArgsConstructor
public class UserService {
    private final ApiUserDS apiUserDS;
    private final BeanMapper beanMapper;

    public UserDTO createProfile(Jwt jwt, CreateProfileRequestDTO dto) throws Exception {
        //todo: check jwt?
        UUID userId = UUID.fromString(jwt.getSubject());
        if(apiUserDS.getById(userId) != null) throw new Exception("todo some explanation | profile already completed");

        ApiUser apiUser = apiUserDS.insert(//todo prepare the approach, how to work dates
                ApiUser.builder()
                        .authUserId(userId)
                        .nickName(dto.getNickName())
                        .dayOfBirth(dto.getDayOfBirth().toInstant())
                        .build());

        return beanMapper.map(apiUser, UserDTO.class);
    }
}