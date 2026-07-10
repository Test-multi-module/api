package net.testproj.api.services;

import net.testproj.api.DTOs.UserDTO;
import net.testproj.db.api.ApiUserDS;
import io.beanmapper.BeanMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserService {
    private final BeanMapper beanMapper;
    private final ApiUserDS apiUserDS;

    public UserDTO create(UserDTO userDTO) {
        //todo
        return new UserDTO();
    }
}