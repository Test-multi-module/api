package net.testproj.api.services;

import net.testproj.api.dtos.models.TestObjToBeDeletedDTO;

import net.testproj.db.auth.OAuth2AccountDS;
import io.beanmapper.BeanMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@AllArgsConstructor
public class TestServiceToCheckHowToUseBeanMapperAndCallDsServices {

    private final BeanMapper beanMapper;
    private final OAuth2AccountDS oAuth2AccountDS;//TODO final or Autowire

    public List<TestObjToBeDeletedDTO> list() {
        return  beanMapper.map(oAuth2AccountDS.list(), TestObjToBeDeletedDTO.class);
    }

    public TestObjToBeDeletedDTO findById(UUID prdId){
        return beanMapper.map(oAuth2AccountDS.findById(prdId), TestObjToBeDeletedDTO.class);
    }
}