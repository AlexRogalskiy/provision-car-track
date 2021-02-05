package com.provision.cartrack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

import com.provision.cartrack.actions.tasks.Task;
import com.provision.cartrack.actions.tasks.TaskRequest;
import com.provision.cartrack.helpers.SimpleResponse;

@SpringBootTest(classes = ProvisionCarTrackApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(statements = { "delete from tasks", "delete from events" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class TaskTests extends RestApiTest {

	@BeforeAll
	void setup(@Autowired DataSource dataSource) throws SQLException {
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("basic_data.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("tasks_basic_data.sql"));
		}
		login();
	}

	@AfterAll
	void destroy(@Autowired DataSource dataSource) throws SQLException {
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("tasks_remove_basic_data.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("remove_basic_data.sql"));
		}
	}

	@Test
	public void testCreateTask() throws Exception {
		// CREATE
		TaskRequest taskRequest = new TaskRequest();
		taskRequest.setExecutionDate(new SimpleDateFormat("yyyy/MM/dd").parse("2020/02/18"));
		taskRequest.setExternalReference("ref1");
		taskRequest.setOdometerValue(15000);
		taskRequest.setTaskType("OIL CHANGE");
		taskRequest.setVehicleSerialNumber("ALB1234");
		ResponseEntity<CreateTaskResponse> addResponse = this.restTemplate.postForEntity(base() + "/tasks", taskRequest,
				CreateTaskResponse.class);
		assertEquals(200, addResponse.getStatusCodeValue());
		Task task1 = addResponse.getBody().payload;
		assertNotNull(task1.getId());

		// CREATE
		taskRequest = new TaskRequest();
		taskRequest.setExecutionDate(new SimpleDateFormat("yyyy/MM/dd").parse("2019/01/09"));
		taskRequest.setExternalReference("ref2");
		taskRequest.setOdometerValue(30000);
		taskRequest.setTaskType("BATTERY REPLACEMENT");
		taskRequest.setVehicleSerialNumber("RED1234");
		addResponse = this.restTemplate.postForEntity(base() + "/tasks", taskRequest, CreateTaskResponse.class);
		assertEquals(200, addResponse.getStatusCodeValue());
		Task task2 = addResponse.getBody().payload;
		assertNotNull(task2.getId());

		// FIND ALL
		ResponseEntity<Task[]> tasks = this.restTemplate.getForEntity(base() + "/tasks", Task[].class);
		assertEquals(2, tasks.getBody().length);

		// FIND BY ID
		ResponseEntity<Task> find = this.restTemplate.getForEntity(base() + "/tasks/" + task1.getId(), Task.class);
		assertEquals(200, find.getStatusCodeValue());
		assertEquals(task1.getId(), find.getBody().getId());

		// FIND BY REF
		find = this.restTemplate.getForEntity(base() + "/tasks/by-external-ref/" + task2.getExternalReference(),
				Task.class);
		assertEquals(200, find.getStatusCodeValue());
		assertEquals(task2.getId(), find.getBody().getId());

		// NOT FOUND
		ResponseEntity<Task> notFound = this.restTemplate.getForEntity(base() + "/tasks/" + 1111, Task.class);
		assertEquals(404, notFound.getStatusCodeValue());

		// NOT FOUND BY REF
		notFound = this.restTemplate.getForEntity(base() + "/tasks/by-external-ref/aaaaaa", Task.class);
		assertEquals(404, notFound.getStatusCodeValue());
	}

	@Test
	public void testUpdateTask() throws Exception {
		// CREATE
		TaskRequest taskRequest = new TaskRequest();
		taskRequest.setExecutionDate(new SimpleDateFormat("yyyy/MM/dd").parse("2020/02/18"));
		taskRequest.setExternalReference("ref1");
		taskRequest.setOdometerValue(15000);
		taskRequest.setTaskType("OIL CHANGE");
		taskRequest.setVehicleSerialNumber("ALB1234");
		ResponseEntity<CreateTaskResponse> addResponse = this.restTemplate.postForEntity(base() + "/tasks", taskRequest,
				CreateTaskResponse.class);
		assertEquals(200, addResponse.getStatusCodeValue());
		Task task1 = addResponse.getBody().payload;
		assertNotNull(task1.getId());

		// FIND BY REF
		ResponseEntity<Task> find = this.restTemplate
				.getForEntity(base() + "/tasks/by-external-ref/" + task1.getExternalReference(), Task.class);
		assertEquals(200, find.getStatusCodeValue());
		assertEquals(task1.getId(), find.getBody().getId());

		// UPDATE
		TaskRequest taskUpdRequest = new TaskRequest();
		taskUpdRequest.setExecutionDate(new SimpleDateFormat("yyyy/MM/dd").parse("2020/02/19"));
		taskUpdRequest.setExternalReference(task1.getExternalReference());
		taskUpdRequest.setOdometerValue(15001);
		taskUpdRequest.setTaskType("TIRE ROTATION");
		taskUpdRequest.setVehicleSerialNumber("ALB1234");

		ResponseEntity<CreateTaskResponse> updResponse = restTemplate.exchange(base() + "/tasks/", HttpMethod.PUT,
				new HttpEntity<TaskRequest>(taskUpdRequest), CreateTaskResponse.class);
		assertEquals(200, updResponse.getStatusCodeValue());
		Task updTask = updResponse.getBody().payload;
		assertEquals(updTask.getExternalReference(), task1.getExternalReference());
		assertEquals(updTask.getId(), task1.getId());
		assertNotEquals(updTask.getOdometerValue(), task1.getOdometerValue());
		assertNotEquals(updTask.getExecutionDate(), task1.getExecutionDate());
		assertNotEquals(updTask.getTaskType(), task1.getTaskType());

		// FIND ALL
		ResponseEntity<Task[]> tasks = this.restTemplate.getForEntity(base() + "/tasks", Task[].class);
		assertEquals(1, tasks.getBody().length);
	}

	@Test
	public void testDeleteTask() throws Exception {
		// CREATE
		TaskRequest taskRequest = new TaskRequest();
		taskRequest.setExecutionDate(new SimpleDateFormat("yyyy/MM/dd").parse("2018/12/25"));
		taskRequest.setExternalReference("ref1");
		taskRequest.setOdometerValue(15000);
		taskRequest.setTaskType("OIL CHANGE");
		taskRequest.setVehicleSerialNumber("ALB1234");
		ResponseEntity<CreateTaskResponse> addResponse = this.restTemplate.postForEntity(base() + "/tasks", taskRequest,
				CreateTaskResponse.class);
		assertEquals(200, addResponse.getStatusCodeValue());
		Task task1 = addResponse.getBody().payload;
		assertNotNull(task1.getId());

		// DELETE
		ResponseEntity<SimpleResponse> deleteResponse = restTemplate.exchange(base() + "/tasks/" + task1.getId(),
				HttpMethod.DELETE, null, SimpleResponse.class);

		assertEquals(200, deleteResponse.getStatusCodeValue());
		assertEquals("Task Deleted", deleteResponse.getBody().getMessage());

		// FIND ALL
		ResponseEntity<Task[]> readings = this.restTemplate.getForEntity(base() + "/tasks", Task[].class);
		assertEquals(0, readings.getBody().length);
	}

	@Test
	public void testTaskInvariants() throws Exception {
		TaskRequest taskRequest = new TaskRequest();
		// cannot put exec date in the future
		taskRequest.setExecutionDate(new SimpleDateFormat("yyyy/MM/dd").parse("2099/12/25"));
		taskRequest.setExternalReference("ref1");
		taskRequest.setOdometerValue(15000);
		taskRequest.setTaskType("OIL CHANGE");
		taskRequest.setVehicleSerialNumber("RED1234");
		ResponseEntity<CreateTaskResponse> addResponse = this.restTemplate.postForEntity(base() + "/tasks", taskRequest,
				CreateTaskResponse.class);
		assertEquals(500, addResponse.getStatusCodeValue());

		taskRequest = new TaskRequest();
		// cannot OIL CHANGE AN ELETRIC CAR
		taskRequest.setExecutionDate(new SimpleDateFormat("yyyy/MM/dd").parse("2000/12/25"));
		taskRequest.setExternalReference("ref1");
		taskRequest.setOdometerValue(15000);
		taskRequest.setTaskType("OIL CHANGE");
		taskRequest.setVehicleSerialNumber("RED1234");
		addResponse = this.restTemplate.postForEntity(base() + "/tasks", taskRequest, CreateTaskResponse.class);
		assertEquals(500, addResponse.getStatusCodeValue());
	}

	@Test
	public void testBulkImport() throws Exception {
		// external ref | serial number | date | odometer | task type

		String data = "ref1|ALB1234|2020/01/09 00:00:00|32000|OIL CHANGE\n" + //
				"ref2|ALB1234|2020/01/10 00:00:00|32200|TIRE ROTATION\n" + //
				"ref3|RED1234|2020/01/15 00:00:00|156000|TIRE ROTATION\n" + //
				"ref4|RED1234|2020/01/16 00:00:00|156500|BATTERY REPLACEMENT\n";

		ResponseEntity<SimpleResponse> response = this.restTemplate.postForEntity(base() + "/tasks/bulk-import", data,
				SimpleResponse.class);
		assertEquals(200, response.getStatusCodeValue());
		assertEquals(response.getBody().getMessage(), "Bulk Import Finished " + 4 + " lines");

		// FIND ALL
		ResponseEntity<Task[]> readings = this.restTemplate.getForEntity(base() + "/tasks", Task[].class);
		assertEquals(4, readings.getBody().length);

		// FIND BY REF
		ResponseEntity<Task> find = this.restTemplate.getForEntity(base() + "/tasks/by-external-ref/ref2", Task.class);
		assertEquals(200, find.getStatusCodeValue());
		assertNotNull(find.getBody().getId());
		Task[] arr = readings.getBody();
		Task r1 = Arrays.stream(arr).filter(x -> x.getExternalReference().equals("ref1")).findAny().get();
		Task r2 = Arrays.stream(arr).filter(x -> x.getExternalReference().equals("ref2")).findAny().get();
		Task r3 = Arrays.stream(arr).filter(x -> x.getExternalReference().equals("ref3")).findAny().get();
		Task r4 = Arrays.stream(arr).filter(x -> x.getExternalReference().equals("ref4")).findAny().get();

		assertEquals("OIL CHANGE", r1.getTaskType().getName());
		assertEquals("TIRE ROTATION", r2.getTaskType().getName());
		assertEquals("TIRE ROTATION", r3.getTaskType().getName());
		assertEquals("BATTERY REPLACEMENT", r4.getTaskType().getName());

		// UPDATE VIA BULK IMPORT
		data = "ref1|ALB1234|2020/01/09 00:00:00|32000|TIRE ROTATION\n";

		response = this.restTemplate.postForEntity(base() + "/tasks/bulk-import", data, SimpleResponse.class);
		assertEquals(200, response.getStatusCodeValue());
		assertEquals(response.getBody().getMessage(), "Bulk Import Finished " + 1 + " lines");

		// FIND BY REF
		find = this.restTemplate.getForEntity(base() + "/tasks/by-external-ref/ref1", Task.class);
		assertEquals(200, find.getStatusCodeValue());
		assertEquals("TIRE ROTATION", find.getBody().getTaskType().getName());
	}

	public static class CreateTaskResponse {
		public Task payload;
	}

}