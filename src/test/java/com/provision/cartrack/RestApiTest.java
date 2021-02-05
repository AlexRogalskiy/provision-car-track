package com.provision.cartrack;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import com.provision.cartrack.security.JwtResponse;
import com.provision.cartrack.security.LoginRequest;

public class RestApiTest {

	@LocalServerPort
	private int port;

	@Autowired
	protected TestRestTemplate restTemplate;

	@Value("${test.user.username}")
	private String username;

	@Value("${test.user.password}")
	private String password;

	@Value("${test.base.server}")
	private String server;

	@Value("${test.base.path}")
	private String path;

	void login() {
		LoginRequest login = new LoginRequest();
		login.setUsername(username);
		login.setPassword(password);
		ResponseEntity<JwtResponse> response = this.restTemplate.postForEntity(base() + "/auth/signin", login,
				JwtResponse.class);
		assertEquals(200, response.getStatusCodeValue());

		restTemplate.getRestTemplate().setInterceptors(Collections.singletonList((request, body, execution) -> {
			request.getHeaders().add("Authorization",
					response.getBody().getTokenType() + " " + response.getBody().getAccessToken());
			return execution.execute(request, body);
		}));
	}

	String base() {
		return server + port + path;
	}

}
