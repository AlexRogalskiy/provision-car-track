package com.provision.cartrack.actions.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.provision.cartrack.helpers.CreateResponse;
import com.provision.cartrack.helpers.SimpleResponse;
import com.provision.cartrack.security.UserPrinciple;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

	@Autowired
	TaskService taskService;

	@Autowired
	TaskRepository taskRepository;

	@PostMapping
	public ResponseEntity<CreateResponse<Task>> addTask(@Valid @RequestBody TaskRequest request,
			@AuthenticationPrincipal UserPrinciple user) {

		Task x = taskService.addTask(request, user.getUserEntity());

		return ResponseEntity.ok(new CreateResponse<Task>("Task Added", x));
	}

	@PutMapping
	public ResponseEntity<CreateResponse<Task>> updateTask(@Valid @RequestBody TaskRequest request,
			@AuthenticationPrincipal UserPrinciple user) {

		Task x = taskService.updateTask(request, user.getUserEntity());

		return ResponseEntity.ok(new CreateResponse<Task>("Task Updated", x));
	}

	@PostMapping("/bulk-import")
	public ResponseEntity<SimpleResponse> bulkImport(@RequestBody String data,
			@AuthenticationPrincipal UserPrinciple user) {
		ArrayList<String> lines = new ArrayList<String>();
		try (Scanner scan = new Scanner(data)) {
			while (scan.hasNextLine()) {
				lines.add(scan.nextLine());
			}
		}
		taskService.bulkImport(lines, user.getUserEntity());
		return ResponseEntity.ok(new SimpleResponse("Bulk Import Finished " + lines.size() + " lines"));
	}

	@GetMapping
	public @ResponseBody List<Task> listAll() {
		return taskRepository.findAll();
	}

	@GetMapping("/{id}")
	public @ResponseBody ResponseEntity<Task> get(@PathVariable Long id) {
		return ResponseEntity.of(taskRepository.findById(id));
	}

	@GetMapping("/by-external-ref/{ref}")
	public @ResponseBody ResponseEntity<Task> get(@PathVariable String ref) {
		return ResponseEntity.of(taskRepository.findByExternalReference(ref));

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<SimpleResponse> delete(@PathVariable(value = "id") Long id,
			@AuthenticationPrincipal UserPrinciple user) {

		taskService.deleteTask(id, user.getUserEntity());

		return ResponseEntity.ok(new SimpleResponse("Task Deleted"));
	}

}