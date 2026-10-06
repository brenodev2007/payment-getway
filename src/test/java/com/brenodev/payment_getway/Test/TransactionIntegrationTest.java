package com.brenodev.payment_getway.Test;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(MySqlTestContainerConfig.class)
@ActiveProfiles("test")
class TransactionIntegrationTest {

    @Test
    void contextLoads() {
    }
}