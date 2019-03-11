package de.witcom.splgateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import de.witcom.splgateway.filter.KeyCloakFilter;

@SpringBootApplication
@EnableScheduling
public class SplGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SplGatewayApplication.class, args);
	}
	
}

