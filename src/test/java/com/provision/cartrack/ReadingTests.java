package com.provision.cartrack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import javax.sql.DataSource;

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

import com.provision.cartrack.actions.readings.Reading;
import com.provision.cartrack.actions.readings.ReadingRequest;
import com.provision.cartrack.helpers.SimpleResponse;

@SpringBootTest(classes = ProvisionCarTrackApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(statements = { "delete from readings", "delete from events" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class ReadingTests extends RestApiTest {

	@BeforeAll
	void setup(@Autowired DataSource dataSource) throws SQLException {
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("basic_data.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("readings_basic_data.sql"));
		}
		login();
	}

	@AfterAll
	void destroy(@Autowired DataSource dataSource) throws SQLException {
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("readings_remove_basic_data.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("remove_basic_data.sql"));
		}
	}

	@Test
	public void testCreateReading() throws Exception {
		// CREATE
		ReadingRequest readingRequest = new ReadingRequest();
		readingRequest.setExecutionDate(new SimpleDateFormat("yyyy/MM/dd").parse("2020/02/18"));
		readingRequest.setExternalReference("ref1");
		readingRequest.setOdometerValue(15000);
		readingRequest.setReadingType("ODOMETER");
		readingRequest.setReadingValue(new BigDecimal(15000));
		readingRequest.setVehicleSerialNumber("ALB1234");
		ResponseEntity<CreateReadingResponse> addResponse = this.restTemplate.postForEntity(base() + "/readings",
				readingRequest, CreateReadingResponse.class);
		assertEquals(200, addResponse.getStatusCodeValue());
		Reading reading1 = addResponse.getBody().payload;
		assertNotNull(reading1.getId());

		// CREATE
		readingRequest = new ReadingRequest();
		readingRequest.setExecutionDate(new SimpleDateFormat("yyyy/MM/dd").parse("2019/01/09"));
		readingRequest.setExternalReference("ref2");
		readingRequest.setOdometerValue(30000);
		readingRequest.setReadingType("OIL LEVEL");
		readingRequest.setReadingValue(new BigDecimal(0));
		readingRequest.setVehicleSerialNumber("RED1234");
		addResponse = this.restTemplate.postForEntity(base() + "/readings", readingRequest,
				CreateReadingResponse.class);
		assertEquals(200, addResponse.getStatusCodeValue());
		Reading reading2 = addResponse.getBody().payload;
		assertNotNull(reading2.getId());

		// FIND ALL
		ResponseEntity<Reading[]> readings = this.restTemplate.getForEntity(base() + "/readings", Reading[].class);
		assertEquals(2, readings.getBody().length);

		// FIND BY ID
		ResponseEntity<Reading> find = this.restTemplate.getForEntity(base() + "/readings/" + reading1.getId(),
				Reading.class);
		assertEquals(200, find.getStatusCodeValue());
		assertEquals(reading1.getId(), find.getBody().getId());

		// FIND BY REF
		find = this.restTemplate.getForEntity(base() + "/readings/by-external-ref/" + reading2.getExternalReference(),
				Reading.class);
		assertEquals(200, find.getStatusCodeValue());
		assertEquals(reading2.getId(), find.getBody().getId());

		// NOT FOUND
		ResponseEntity<Reading> notFound = this.restTemplate.getForEntity(base() + "/readings/" + 1111, Reading.class);
		assertEquals(404, notFound.getStatusCodeValue());

		// NOT FOUND BY REF
		notFound = this.restTemplate.getForEntity(base() + "/readings/by-external-ref/aaaaaa", Reading.class);
		assertEquals(404, notFound.getStatusCodeValue());
	}

	@Test
	public void testUpdateReading() throws Exception {
		// CREATE
		ReadingRequest readingRequest = new ReadingRequest();
		readingRequest.setExecutionDate(new SimpleDateFormat("yyyy/MM/dd").parse("2020/02/18"));
		readingRequest.setExternalReference("ref1");
		readingRequest.setOdometerValue(15000);
		readingRequest.setReadingType("ODOMETER");
		readingRequest.setReadingValue(new BigDecimal(15000));
		readingRequest.setVehicleSerialNumber("ALB1234");
		ResponseEntity<CreateReadingResponse> addResponse = this.restTemplate.postForEntity(base() + "/readings",
				readingRequest, CreateReadingResponse.class);
		assertEquals(200, addResponse.getStatusCodeValue());
		Reading reading1 = addResponse.getBody().payload;
		assertNotNull(reading1.getId());

		// FIND BY REF
		ResponseEntity<Reading> find = this.restTemplate
				.getForEntity(base() + "/readings/by-external-ref/" + reading1.getExternalReference(), Reading.class);
		assertEquals(200, find.getStatusCodeValue());
		assertEquals(reading1.getId(), find.getBody().getId());

		// UPDATE
		ReadingRequest readingUpdRequest = new ReadingRequest();
		readingUpdRequest.setExecutionDate(new SimpleDateFormat("yyyy/MM/dd").parse("2020/02/19"));
		readingUpdRequest.setExternalReference(reading1.getExternalReference());
		readingUpdRequest.setOdometerValue(15001);
		readingUpdRequest.setReadingType("TIRE THREAD WEAR");
		readingUpdRequest.setReadingValue(new BigDecimal(2));
		readingUpdRequest.setVehicleSerialNumber("ALB1234");

		ResponseEntity<CreateReadingResponse> updResponse = restTemplate.exchange(base() + "/readings/", HttpMethod.PUT,
				new HttpEntity<ReadingRequest>(readingUpdRequest), CreateReadingResponse.class);
		assertEquals(200, updResponse.getStatusCodeValue());
		Reading updReading = updResponse.getBody().payload;
		assertEquals(updReading.getExternalReference(), reading1.getExternalReference());
		assertEquals(updReading.getId(), reading1.getId());
		assertNotEquals(updReading.getOdometerValue(), reading1.getOdometerValue());
		assertNotEquals(updReading.getExecutionDate(), reading1.getExecutionDate());
		assertNotEquals(updReading.getReadingType(), reading1.getReadingType());
		assertNotEquals(updReading.getReadingValue(), reading1.getReadingValue());

		// FIND ALL
		ResponseEntity<Reading[]> readings = this.restTemplate.getForEntity(base() + "/readings", Reading[].class);
		assertEquals(1, readings.getBody().length);
	}

	@Test
	public void testDeleteReading() throws Exception {
		// CREATE
		ReadingRequest readingRequest = new ReadingRequest();
		readingRequest.setExecutionDate(new SimpleDateFormat("yyyy/MM/dd").parse("2018/12/25"));
		readingRequest.setExternalReference("ref1");
		readingRequest.setOdometerValue(15000);
		readingRequest.setReadingType("ODOMETER");
		readingRequest.setReadingValue(new BigDecimal(15000));
		readingRequest.setVehicleSerialNumber("ALB1234");
		ResponseEntity<CreateReadingResponse> addResponse = this.restTemplate.postForEntity(base() + "/readings",
				readingRequest, CreateReadingResponse.class);
		assertEquals(200, addResponse.getStatusCodeValue());
		Reading reading1 = addResponse.getBody().payload;
		assertNotNull(reading1.getId());

		// DELETE
		ResponseEntity<SimpleResponse> deleteResponse = restTemplate.exchange(base() + "/readings/" + reading1.getId(),
				HttpMethod.DELETE, null, SimpleResponse.class);

		assertEquals(200, deleteResponse.getStatusCodeValue());
		assertEquals("Reading Deleted", deleteResponse.getBody().getMessage());

		// FIND ALL
		ResponseEntity<Reading[]> readings = this.restTemplate.getForEntity(base() + "/readings", Reading[].class);
		assertEquals(0, readings.getBody().length);
	}

	@Test
	public void testInvariants() throws Exception {
		// CREATE
		ReadingRequest readingRequest = new ReadingRequest();
		// cannot put exec date in the future
		readingRequest.setExecutionDate(new SimpleDateFormat("yyyy/MM/dd").parse("2099/12/25"));
		readingRequest.setExternalReference("ref1");
		readingRequest.setOdometerValue(15000);
		readingRequest.setReadingType("ODOMETER");
		readingRequest.setReadingValue(new BigDecimal(15000));
		readingRequest.setVehicleSerialNumber("ALB1234");
		ResponseEntity<CreateReadingResponse> addResponse = this.restTemplate.postForEntity(base() + "/readings",
				readingRequest, CreateReadingResponse.class);
		assertEquals(500, addResponse.getStatusCodeValue());
	}

	@Test
	public void testBulkImport() throws Exception {
		// external ref | serial number | date | type | odometer | reading value

		String data = "ref1|ALB1234|2020/01/09 00:00:00|ODOMETER|32000|32000\n" + //
				"ref2|ALB1234|2020/01/10 00:00:00|ODOMETER|32200|32200\n" + //
				"ref3|RED1234|2020/01/15 00:00:00|ODOMETER|156000|156000\n" + //
				"ref4|RED1234|2020/01/16 00:00:00|ODOMETER|156500|156500\n";

		ResponseEntity<SimpleResponse> response = this.restTemplate.postForEntity(base() + "/readings/bulk-import",
				data, SimpleResponse.class);
		assertEquals(200, response.getStatusCodeValue());
		assertEquals(response.getBody().getMessage(), "Bulk Import Finished " + 4 + " lines");

		// FIND ALL
		ResponseEntity<Reading[]> readings = this.restTemplate.getForEntity(base() + "/readings", Reading[].class);
		assertEquals(4, readings.getBody().length);

		// FIND BY REF
		ResponseEntity<Reading> find = this.restTemplate.getForEntity(base() + "/readings/by-external-ref/ref2",
				Reading.class);
		assertEquals(200, find.getStatusCodeValue());
		assertNotNull(find.getBody().getId());
		Reading[] arr = readings.getBody();
		Reading r1 = Arrays.stream(arr).filter(x -> x.getExternalReference().equals("ref1")).findAny().get();
		Reading r2 = Arrays.stream(arr).filter(x -> x.getExternalReference().equals("ref2")).findAny().get();
		Reading r3 = Arrays.stream(arr).filter(x -> x.getExternalReference().equals("ref3")).findAny().get();
		Reading r4 = Arrays.stream(arr).filter(x -> x.getExternalReference().equals("ref4")).findAny().get();

		assertEquals(new BigDecimal("32000.00"), r1.getReadingValue());
		assertEquals(new BigDecimal("32200.00"), r2.getReadingValue());
		assertEquals(new BigDecimal("156000.00"), r3.getReadingValue());
		assertEquals(new BigDecimal("156500.00"), r4.getReadingValue());

		// UPDATE VIA BULK IMPORT
		data = "ref1|ALB1234|2020/01/09 00:00:00|ODOMETER|32001|32001";

		response = this.restTemplate.postForEntity(base() + "/readings/bulk-import", data, SimpleResponse.class);
		assertEquals(200, response.getStatusCodeValue());
		assertEquals(response.getBody().getMessage(), "Bulk Import Finished " + 1 + " lines");

		// FIND BY REF
		find = this.restTemplate.getForEntity(base() + "/readings/by-external-ref/ref1", Reading.class);
		assertEquals(200, find.getStatusCodeValue());
		assertEquals(new BigDecimal("32001.00"), find.getBody().getReadingValue());

	}

	public static class CreateReadingResponse {
		public Reading payload;
	}

}