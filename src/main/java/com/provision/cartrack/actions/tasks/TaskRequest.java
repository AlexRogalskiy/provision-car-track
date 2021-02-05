package com.provision.cartrack.actions.tasks;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.provision.cartrack.helpers.JSONable;

public class TaskRequest implements JSONable {

	@NotBlank
	@Size(min = 3, max = 60)
	private String vehicleSerialNumber;

	@NotBlank
	private String taskType;

	@NotNull
	private Date executionDate;

	@NotBlank
	private String externalReference;

	@NotNull
	@Min(0)
	private Integer odometerValue;

	@Override
	public String toJSON() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public String getVehicleSerialNumber() {
		return vehicleSerialNumber;
	}

	public void setVehicleSerialNumber(String vehicleSerialNumber) {
		this.vehicleSerialNumber = vehicleSerialNumber;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public Date getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}

	public Integer getOdometerValue() {
		return odometerValue;
	}

	public void setOdometerValue(Integer odometerValue) {
		this.odometerValue = odometerValue;
	}

}
