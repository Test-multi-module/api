package com.testproj.api.services;

import com.testproj.api.dtos.models.UserDTO;
import com.testproj.db.pb.UserDS;
import io.beanmapper.BeanMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserService {
    private final BeanMapper beanMapper;
    private final UserDS userDS;

    public UserDTO create(UserDTO userDTO) {
        //todo
        return new UserDTO();
    }
}