package com.provision.cartrack.registry.vehicles;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.provision.cartrack.registry.EngineTypeRepository;
import com.provision.cartrack.registry.models.Model;
import com.provision.cartrack.registry.models.ModelRepository;
import com.provision.cartrack.security.User;

@Service
public class VehicleService {

	@Autowired
	ModelRepository modelRepository;

	@Autowired
	EngineTypeRepository engineTypeRepository;

	@Autowired
	VehicleRepository vehicleRepository;

	public Vehicle addVehicle(AddVehicleRequest request, User user) {
		Model model = modelRepository.findById(request.getModelId())
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Make" + request.getModelId() + " not found."));

		Optional<Vehicle> exists = vehicleRepository.findBySerialNumber(request.getSerialNumber());

		if (exists.isPresent())
			throw new IllegalArgumentException("This Vehicle already exists with ID " + exists.get().getId());

		Vehicle x = new Vehicle();
		x.setModel(model);
		x.setSerialNumber(request.getSerialNumber());
		x.auditCreation(user);
		x = vehicleRepository.save(x);

		return x;
	}

	public Vehicle updateVehicle(UpdateVehicleRequest request) {
		Vehicle x = vehicleRepository.findById(request.getId())
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Vehicle " + request.getId() + " not found."));

		Model model = modelRepository.findById(request.getModelId())
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Make" + request.getModelId() + " not found."));

		Optional<Vehicle> exists = vehicleRepository.findBySerialNumber(request.getSerialNumber());

		if (exists.isPresent() && exists.get().getId().longValue() != request.getId().longValue())
			throw new IllegalArgumentException("This Vehicle already exists with ID " + exists.get().getId());

		x.setModel(model);
		x.setSerialNumber(request.getSerialNumber());
		x = vehicleRepository.save(x);

		return x;
	}

	public void deleteVehicle(Long id) {
		Vehicle Vehicle = vehicleRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Vehicle " + id + " not found."));

		vehicleRepository.delete(Vehicle);
	}

}
