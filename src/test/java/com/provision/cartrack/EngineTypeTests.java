package com.provision.cartrack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.logging.log4j.message.SimpleMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.provision.cartrack.registry.EngineType;

@SpringBootTest(classes = ProvisionCarTrackApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(statements = { "delete from engine_types" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class EngineTypeTests extends RestApiTest {

	@BeforeAll
	void setup(@Autowired DataSource dataSource) throws SQLException {
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("basic_data.sql"));
		}
		login();
	}

	@AfterAll
	void destroy(@Autowired DataSource dataSource) throws SQLException {
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("remove_basic_data.sql"));
		}
	}

	@Test
	public void testEngineTypeCRUDWorkflow() {
		// CREATE
		EngineType engine = new EngineType();
		engine.setName("DIESEL");
		ResponseEntity<CreateEngineTypeResponse> addResponse = this.restTemplate.postForEntity(base() + "/enginetypes",
				engine, CreateEngineTypeResponse.class);
		assertEquals(200, addResponse.getStatusCodeValue());
		EngineType payload = addResponse.getBody().payload;
		assertNotNull(payload.getId());

		engine.setName("ELETRIC");
		addResponse = this.restTemplate.postForEntity(base() + "/enginetypes", engine, CreateEngineTypeResponse.class);
		assertEquals(200, addResponse.getStatusCodeValue());
		payload = addResponse.getBody().payload;
		assertNotNull(payload.getId());

		// FIND ALL
		ResponseEntity<EngineType[]> listAll = this.restTemplate.getForEntity(base() + "/enginetypes",
				EngineType[].class);
		assertEquals(2, listAll.getBody().length);

		String engine1 = listAll.getBody()[0].getId().toString();
		String engine2 = listAll.getBody()[1].getId().toString();

		// DELETE
		this.restTemplate.delete(base() + "/enginetypes/" + engine1);
		listAll = this.restTemplate.getForEntity(base() + "/enginetypes", EngineType[].class);
		assertEquals(1, listAll.getBody().length);

		// NOT FOUND
		ResponseEntity<EngineType> notFound = this.restTemplate.getForEntity(base() + "/enginetypes/" + engine1,
				EngineType.class);
		assertEquals(404, notFound.getStatusCodeValue());

		// FIND ONE
		ResponseEntity<EngineType> findOne = this.restTemplate.getForEntity(base() + "/enginetypes/" + engine2,
				EngineType.class);
		assertEquals(200, findOne.getStatusCodeValue());
		assertNotNull(findOne.getBody().getId());

		// UPDATE
		EngineType engineUpd = new EngineType();
		engineUpd.setName("GASOLINE");
		ResponseEntity<SimpleMessage> responseUpd = restTemplate.exchange(base() + "/enginetypes/" + engine2,
				HttpMethod.PUT, new HttpEntity<EngineType>(engineUpd), SimpleMessage.class);
		assertEquals(200, responseUpd.getStatusCodeValue());

		// FIND AGAIN
		findOne = this.restTemplate.getForEntity(base() + "/enginetypes/" + engine2, EngineType.class);
		assertEquals(200, findOne.getStatusCodeValue());
		assertEquals(engine2.toString(), findOne.getBody().getId().toString());
	}

	@Test
	public void testUniqueEngineName() {
		EngineType engine = new EngineType();
		engine.setName("DIESEL");
		ResponseEntity<CreateEngineTypeResponse> addResponse = this.restTemplate.postForEntity(base() + "/enginetypes",
				engine, CreateEngineTypeResponse.class);
		assertEquals(200, addResponse.getStatusCodeValue());
		EngineType payload = addResponse.getBody().payload;
		assertNotNull(payload.getId());

		addResponse = this.restTemplate.postForEntity(base() + "/enginetypes", engine, CreateEngineTypeResponse.class);
		assertEquals(500, addResponse.getStatusCodeValue());
	}

	public static class CreateEngineTypeResponse {
		public EngineType payload;
	}

}