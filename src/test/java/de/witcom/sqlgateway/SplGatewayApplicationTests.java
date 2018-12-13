package de.witcom.sqlgateway;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.witcom.splgateway.SessionManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SplGatewayApplicationTests {
	
	@Autowired
	SessionManager sessionManager;

	@Test
	@Ignore
	public void contextLoads() {
		
		String sessionId = sessionManager.getSessionId();
	}

}

