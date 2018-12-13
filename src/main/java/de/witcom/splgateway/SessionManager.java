package de.witcom.splgateway;

import java.net.HttpCookie;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import de.witcom.splgateway.swagger.model.BooleanHolder;
import de.witcom.splgateway.swagger.model.UserLoginDto;

@Service
public class SessionManager {

	@Value("${splUri}")
	private String splUri;

	Logger logger = LoggerFactory.getLogger(SessionManager.class);

	private String sessionId = null;

	private void refreshSession() {
		if (sessionId == null) {
			this.login();
			return;
		}
		if (isSessionActive()) {
			return;
		}
		logger.warn("Session {} expired - refresh required", this.sessionId);
		this.login();
	}

	private boolean isSessionActive() {
		// logger.debug("Check if Session {} is active",this.sessionId);
		String url = splUri + "/serviceplanet/remote/service/v1/login/logged_in_user/active";
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		requestHeaders.set("Cookie", "JSESSIONID=" + this.sessionId);
		HttpEntity<Void> request = new HttpEntity<>(requestHeaders);

		try {
			ResponseEntity<BooleanHolder> response = restTemplate.exchange(url, HttpMethod.GET, request,
					BooleanHolder.class);
			if (!response.getStatusCode().equals(HttpStatus.OK)) {
				logger.error("Login to ServicePlanet was not successful - got Status : {}", response.getStatusCode());
			} else {
				return response.getBody().isValue();
			}
		} catch (Exception e) {
			logger.error("Error when trying to validate session in SPL: {}", e.getMessage());
		}

		return false;
	}

	private void login() {

		String url = splUri + "/serviceplanet/remote/service/v1/login/authenticate";

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		//toDo get from environment
		map.add("loginname", "auser");
		map.add("password", "apassword");
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
				requestHeaders);
		try {
			ResponseEntity<Object> response = restTemplate.postForEntity(url, request, Object.class);
			if (!response.getStatusCode().equals(HttpStatus.OK)) {
				logger.error("Login to ServicePlanet was not successful - got Status : {}", response.getStatusCode());
			} else {
				if (response.getHeaders().get("Set-Cookie").isEmpty()) {
					logger.error("Unable to extract sessionid - got no cookies");
				} else {

					String cookieValue = null;
					for (String cookie : response.getHeaders().get("set-cookie")) {
						List<HttpCookie> cookies = HttpCookie.parse(cookie);
						if (cookies.get(0).getName().equals("JSESSIONID")) {
							cookieValue = cookies.get(0).getValue();
						}
					}
					if (cookieValue == null) {
						logger.error("Unable to login - no session cookie");
					} else {
						this.sessionId = cookieValue;
					}
				}
			}

		} catch (Exception e) {
			logger.error("Error when trying to login to SPL: {}", e.getMessage());

		}
	}

	public String getSessionId() {
		refreshSession();
		return sessionId;
	}

}
