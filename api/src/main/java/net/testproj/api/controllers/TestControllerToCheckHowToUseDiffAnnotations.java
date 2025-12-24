package net.testproj.api.controllers;

import net.testproj.api.services.TestServiceToCheckHowToUseBeanMapperAndCallDsServices;
import net.testproj.api.dtos.models.TestObjToBeDeletedDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/private/products")
public class TestControllerToCheckHowToUseDiffAnnotations {
    private final TestServiceToCheckHowToUseBeanMapperAndCallDsServices testServiceToCheckHowToUseBeanMapperAndCallDsServices;
    public TestControllerToCheckHowToUseDiffAnnotations(TestServiceToCheckHowToUseBeanMapperAndCallDsServices testServiceToCheckHowToUseBeanMapperAndCallDsServices) {this.testServiceToCheckHowToUseBeanMapperAndCallDsServices = testServiceToCheckHowToUseBeanMapperAndCallDsServices;}

    @PreAuthorize("hasRole('ADMIN')")//todo explore how it works
    @GetMapping("/{id}")
    public TestObjToBeDeletedDTO getSomeProductById(@PathVariable("id") UUID id) {
        return testServiceToCheckHowToUseBeanMapperAndCallDsServices.findById(id);
    }

    @GetMapping()
    public List<TestObjToBeDeletedDTO> getProducts(){return testServiceToCheckHowToUseBeanMapperAndCallDsServices.list();}
    @GetMapping("/search")
    public List<TestObjToBeDeletedDTO> searchProducts(@RequestBody Object someMyCustomFilterObjectDto){
        return null;
    }

    @PutMapping(value = "/update/{id}")
    public TestObjToBeDeletedDTO update(@PathVariable("id") Integer id,
                                        @RequestBody TestObjToBeDeletedDTO product){ return null; }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") Integer id){
    }}