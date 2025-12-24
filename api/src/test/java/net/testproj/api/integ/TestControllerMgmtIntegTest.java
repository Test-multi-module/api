package net.testproj.api.integ;

import net.testproj.api.ApiApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;


@SpringBootTest(classes = ApiApplication.class ,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestControllerMgmtIntegTest extends IntegTestBase{

    @LocalServerPort private int port;

    @Autowired private TestRestTemplate restTemplate;

    //todo
}