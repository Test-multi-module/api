package net.testproj.api.unit;

import net.testproj.api.ApiApplication;
import net.testproj.api.controllers.TestControllerToCheckHowToUseDiffAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = ApiApplication.class)
class ApiApplicationTests {

    @Autowired
    TestControllerToCheckHowToUseDiffAnnotations testControllerToCheckHowToUseDiffAnnotations;

    public static void main(String[] args) {
        String classpath = System.getProperty("java.class.path");
        System.out.println("Classpath: " + classpath);
    }
}