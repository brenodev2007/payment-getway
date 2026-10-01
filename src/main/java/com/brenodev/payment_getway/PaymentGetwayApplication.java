package com.brenodev.payment_getway;

import com.brenodev.payment_getway.Config.OutboxProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties(OutboxProperties.class)
public class PaymentGetwayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentGetwayApplication.class, args);
	}

}
