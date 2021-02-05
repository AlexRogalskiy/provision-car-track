package com.provision.cartrack.actions.tasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.provision.cartrack.actions.Event;
import com.provision.cartrack.actions.Event.EventType;
import com.provision.cartrack.actions.EventRepository;
import com.provision.cartrack.helpers.JSONable;
import com.provision.cartrack.registry.TaskType;
import com.provision.cartrack.registry.TaskTypeRepository;
import com.provision.cartrack.registry.vehicles.Vehicle;
import com.provision.cartrack.registry.vehicles.VehicleRepository;
import com.provision.cartrack.security.User;

@Service
public class TaskService {

	@Autowired
	private TaskRepository TaskRepository;

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private TaskTypeRepository TaskTypeRepository;

	@Autowired
	private EventRepository eventRepository;

	public Task addTask(TaskRequest request, User user) {
		Vehicle vehicle = vehicleRepository.findBySerialNumber(request.getVehicleSerialNumber())
				.orElseThrow(() -> new RuntimeException(
						"Fail! -> Cause: Vehicle " + request.getVehicleSerialNumber() + " not found."));

		TaskType TaskType = TaskTypeRepository.findByName(request.getTaskType()).orElseThrow(
				() -> new RuntimeException("Fail! -> Cause: TaskType " + request.getTaskType() + " not found."));

		if (request.getExecutionDate().after(new Date()))
			throw new IllegalArgumentException("Execution Date cannot be in the future.");

		Task x = new Task();
		Optional<Task> exists = TaskRepository.findByExternalReference(request.getExternalReference());
		if (exists.isPresent()) {
			throw new RuntimeException(
					"Fail! -> Cause: External Reference " + request.getExternalReference() + " already exists.");
		}

		x.setVehicle(vehicle);
		x.setTaskType(TaskType);
		x.setExecutionDate(request.getExecutionDate());
		x.setOdometerValue(request.getOdometerValue());
		x.setExternalReference(request.getExternalReference());
		x.auditCreation(user);

		x.invariantsValidation();

		x = TaskRepository.save(x);

		eventRepository.save(new Event(user, EventType.TASK_ADDED, request));

		return x;
	}

	public Task updateTask(TaskRequest request, User user) {
		Vehicle vehicle = vehicleRepository.findBySerialNumber(request.getVehicleSerialNumber())
				.orElseThrow(() -> new RuntimeException(
						"Fail! -> Cause: Vehicle " + request.getVehicleSerialNumber() + " not found."));

		TaskType TaskType = TaskTypeRepository.findByName(request.getTaskType()).orElseThrow(
				() -> new RuntimeException("Fail! -> Cause: TaskType " + request.getTaskType() + " not found."));

		Task x = TaskRepository.findByExternalReference(request.getExternalReference()).orElseThrow(
				() -> new RuntimeException("Fail! -> Cause: Task " + request.getExternalReference() + " not found."));

		if (request.getExecutionDate().after(new Date()))
			throw new IllegalArgumentException("Execution Date cannot be in the future.");

		x.setVehicle(vehicle);
		x.setTaskType(TaskType);
		x.setExecutionDate(request.getExecutionDate());
		x.setOdometerValue(request.getOdometerValue());
		x.auditCreation(user);

		x.invariantsValidation();

		x = TaskRepository.save(x);

		eventRepository.save(new Event(user, EventType.TASK_UPDATED, request));

		return x;
	}

	/**
	 * Imports a batch of Tasks in the following format using the PIPE separator
	 * 
	 * // external ref | serial number | date | odometer| task type
	 * 
	 * IF the Task already exists it will be updated
	 *
	 **/
	public void bulkImport(List<String> TasksLines, User user) {
		TasksLines.forEach(line -> {
			String[] values = line.split("\\|");
			int colIdx = 0;
			TaskRequest rr = new TaskRequest();
			rr.setExternalReference((values[colIdx++].trim()));
			rr.setVehicleSerialNumber(values[colIdx++].trim());
			String dateval = values[colIdx++].trim();
			try {
				rr.setExecutionDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateval));
			} catch (ParseException e) {
				throw new RuntimeException("Bad Date " + dateval);
			}
			rr.setOdometerValue(Integer.parseInt(values[colIdx++].trim()));
			rr.setTaskType(values[colIdx++].trim());
			Boolean exists = TaskRepository.existsByExternalReference(rr.getExternalReference());
			if (!exists) {
				addTask(rr, user);
			} else {
				updateTask(rr, user);
			}
		});
	}

	public void deleteTask(Long id, User user) {
		Task x = TaskRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Task " + id + " not found."));

		TaskRepository.delete(x);
		DeleteRequest dr = new DeleteRequest();
		dr.taskId = id;
		eventRepository.save(new Event(user, EventType.TASK_REMOVED, dr));
	}

	private static class DeleteRequest implements JSONable {
		@SuppressWarnings("unused")
		public Long taskId;

		public String toJSON() {
			try {
				return new ObjectMapper().writeValueAsString(this);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
