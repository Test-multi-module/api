package com.testproj.api.unit;

import com.testproj.api.ApiApplication;
import com.testproj.api.controllers.ProductController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ApiApplication.class)
class ApiApplicationTests {

    @Autowired ProductController productController;
}