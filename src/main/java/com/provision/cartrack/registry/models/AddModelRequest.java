package com.provision.cartrack.registry.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.provision.cartrack.helpers.JSONable;

public class AddModelRequest implements JSONable {

	@NotBlank
	private String name;

	@NotNull
	@Min(1950)
	private Integer year;

	@NotNull
	private Long makeId;

	@NotNull
	private Long engineTypeId;

	@Override
	public String toJSON() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Long getMakeId() {
		return makeId;
	}

	public void setMakeId(Long makeId) {
		this.makeId = makeId;
	}

	public Long getEngineTypeId() {
		return engineTypeId;
	}

	public void setEngineTypeId(Long engineTypeId) {
		this.engineTypeId = engineTypeId;
	}

}
