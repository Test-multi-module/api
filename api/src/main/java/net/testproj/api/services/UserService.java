package net.testproj.api.services;

import io.beanmapper.BeanMapper;
import net.testproj.api.DTOs.CreateProfileRequestDTO;
import net.testproj.api.DTOs.UserDTO;
import net.testproj.db.api.ApiUserDS;
import lombok.AllArgsConstructor;
import net.testproj.db.api.ApiUser;
import net.testproj.db.auth.AuthUserDS;
import net.testproj.db.auth.AuthUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@AllArgsConstructor
public class UserService {
    private final ApiUserDS apiUserDS;
    private final BeanMapper beanMapper;
    private final AuthUserDS authUserDS;

    public UserDTO createProfile(Jwt jwt, CreateProfileRequestDTO dto) throws Exception {
        UUID userId = UUID.fromString(jwt.getSubject());
        AuthUser authUser = authUserDS.getById(userId);
        if(authUser == null || !Boolean.TRUE.equals(authUser.getProfileCompleted()) ||
                apiUserDS.getById(userId) != null)
            throw new Exception("todo some explanation | user not found | profile already completed");

        ApiUser apiUser = apiUserDS.insert(//todo prepare an approach, how to work dates
                ApiUser.builder().authUserId(userId).dayOfBirth(dto.getDayOfBirth().toInstant()).build());

        return beanMapper.map(apiUser, UserDTO.class);
    }
}