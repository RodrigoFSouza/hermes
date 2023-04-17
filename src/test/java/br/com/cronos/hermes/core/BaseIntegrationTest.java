package br.com.cronos.hermes.core;

import br.com.cronos.hermes.HermesApplication;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.net.MalformedURLException;
import java.net.URL;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;

@ActiveProfiles(profiles = "integration-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = HermesApplication.class)
public abstract class BaseIntegrationTest {

    @LocalServerPort
    private int serverPort;

    protected String basePath;

    @BeforeEach
    void setup() throws MalformedURLException {
        enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = serverPort;

        setupEach();
    }

    protected void setupEach() throws MalformedURLException {}

    protected void setLocalBasePath(String path) throws MalformedURLException {
        this.basePath = new URL("http", "localhost", serverPort, path).toString();
    }
}