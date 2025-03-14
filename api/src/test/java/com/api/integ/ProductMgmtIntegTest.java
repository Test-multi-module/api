package com.api.integ;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = com.api.ApiApplication.class ,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductMgmtIntegTest extends IntegTestBase{

    @LocalServerPort private int port;

    @Autowired private TestRestTemplate restTemplate;

    //todo
}