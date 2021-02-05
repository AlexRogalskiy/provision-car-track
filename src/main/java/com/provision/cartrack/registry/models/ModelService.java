package com.provision.cartrack.registry.models;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.provision.cartrack.registry.EngineType;
import com.provision.cartrack.registry.EngineTypeRepository;
import com.provision.cartrack.registry.Make;
import com.provision.cartrack.registry.MakeRepository;
import com.provision.cartrack.security.User;

@Service
public class ModelService {

	@Autowired
	MakeRepository makeRepository;

	@Autowired
	EngineTypeRepository engineTypeRepository;

	@Autowired
	ModelRepository modelRepository;

	public Model addModel(AddModelRequest request, User user) {
		Make make = makeRepository.findById(request.getMakeId())
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Make" + request.getMakeId() + " not found."));

		EngineType engineType = engineTypeRepository.findById(request.getEngineTypeId()).orElseThrow(
				() -> new RuntimeException("Fail! -> Cause: EngineType" + request.getEngineTypeId() + " not found."));

		Optional<Model> exists = modelRepository.findByNameAndYearAndMake(request.getName(), request.getYear(), make);

		if (exists.isPresent())
			throw new IllegalArgumentException("This Model already exists with ID " + exists.get().getId());

		Model x = new Model();
		x.setMake(make);
		x.setEngineType(engineType);
		x.setName(request.getName());
		x.setYear(request.getYear());
		x.auditCreation(user);
		x = modelRepository.save(x);

		return x;
	}

	public Model updateModel(UpdateModelRequest request) {
		Model x = modelRepository.findById(request.getId())
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Model " + request.getId() + " not found."));

		Make make = makeRepository.findById(request.getMakeId())
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Make" + request.getMakeId() + " not found."));

		EngineType engineType = engineTypeRepository.findById(request.getEngineTypeId()).orElseThrow(
				() -> new RuntimeException("Fail! -> Cause: EngineType" + request.getEngineTypeId() + " not found."));

		Optional<Model> exists = modelRepository.findByNameAndYearAndMake(request.getName(), request.getYear(), make);

		if (exists.isPresent() && exists.get().getId().longValue() != request.getId().longValue())
			throw new IllegalArgumentException("This Model already exists with ID " + exists.get().getId());

		x.setName(request.getName());
		x.setYear(request.getYear());
		x.setMake(make);
		x.setEngineType(engineType);
		x = modelRepository.save(x);

		return x;
	}

	public void deleteModel(Long id) {
		Model model = modelRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Model " + id + " not found."));

		modelRepository.delete(model);
	}

}
