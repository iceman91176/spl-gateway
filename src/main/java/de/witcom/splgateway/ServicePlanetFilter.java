package de.witcom.splgateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.stereotype.Component;



import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;


@Component
public class ServicePlanetFilter extends AbstractGatewayFilterFactory{
	
	@Autowired
	SessionManager sessionManager;
	
	Logger logger = LoggerFactory.getLogger(ServicePlanetFilter.class);

	@Override
	public GatewayFilter apply(Object config) {
		
		return (exchange, chain) -> {
            //ServerHttpRequest request = exchange.getRequest();
			logger.debug("in the filter"); 
			
			String sessionId = sessionManager.getSessionId();
            if (sessionId!=null) {
	            ServerHttpRequest request = exchange.getRequest().mutate()
						.header("Cookie", "JSESSIONID=" + sessionId)
						.build();
	            return chain.filter(exchange.mutate().request(request).build());
            } 
            logger.warn("Got no SPL-Session-ID - API-Call will fail");
            return chain.filter(exchange);
        };
		
	
	}

	

}
