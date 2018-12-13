package de.witcom.splgateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SplGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SplGatewayApplication.class, args);
	}
	
}

