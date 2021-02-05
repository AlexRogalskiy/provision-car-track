package com.provision.cartrack;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.provision.cartrack.helpers.Pagination;
import com.provision.cartrack.registry.models.Model;

@SpringBootTest(classes = ProvisionCarTrackApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(statements = { "delete from models" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class ModelTests extends RestApiTest {

	@BeforeAll
	void setup(@Autowired DataSource dataSource) throws SQLException {
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("basic_data.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("models_basic_data.sql"));
		}
		login();
	}

	@AfterAll
	void destroy(@Autowired DataSource dataSource) throws SQLException {
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("remove_basic_data.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("models_remove_basic_data.sql"));
		}
	}

	@Test
	public void testSearchModels() {
		Pagination<Model> p = new Pagination<Model>();
		Map<String, Object> filters = new HashMap<>();
		p.setFilters(filters);
		p.setCurrentPage(1);
		p.setPageSize(2);
		p.setSortColumn("name");
		p.setSortOrder("ASC");

		ResponseEntity<PaginationResponse> response = restTemplate.postForEntity(base() + "/models/search", p,
				PaginationResponse.class);
		assertEquals(2, response.getBody().getResults().size());

		// TEST FILTERS
		filters = new HashMap<>();
		filters.put("model_name", "ATOR");
		p.setFilters(filters);
		p.setCurrentPage(1);
		p.setPageSize(5);
		p.setSortColumn("model_year");
		p.setSortOrder("DESC");

		response = restTemplate.postForEntity(base() + "/models/search", p, PaginationResponse.class);
		assertEquals(2, response.getBody().getResults().size());
		Model av = response.getBody().getResults().get(0);
		assertEquals("NAVIGATOR", av.getName());

		// TEST PAGINATION ...

		// TEST PAGINATION AND FILTERS...

		// TEST PAGINATION AND FILTERS AND SORTING...
		
		// TEST PAGINATION AND FILTERS AND SORTING AND WRONG CURRENT PAGE...

	}

	public static class PaginationResponse extends Pagination<Model> {
	}

}