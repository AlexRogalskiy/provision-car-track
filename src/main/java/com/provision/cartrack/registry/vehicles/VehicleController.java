package com.provision.cartrack.registry.vehicles;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.provision.cartrack.helpers.CreateResponse;
import com.provision.cartrack.helpers.SimpleResponse;
import com.provision.cartrack.security.UserPrinciple;

@Controller
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

	@Autowired
	VehicleRepository vehicleRepository;

	@Autowired
	VehicleService vehicleService;

	@GetMapping
	public @ResponseBody List<Vehicle> listAll() {
		return this.vehicleRepository.findAll();
	}

	@GetMapping("/{id}")
	public @ResponseBody ResponseEntity<Vehicle> get(@PathVariable Long id) {
		Vehicle value = this.vehicleRepository.findById(id).get();
		if (value == null)
			return new ResponseEntity<Vehicle>(HttpStatus.NOT_FOUND);
		return ResponseEntity.ok().body(value);
	}

	@PostMapping
	public ResponseEntity<CreateResponse<Vehicle>> create(@AuthenticationPrincipal UserPrinciple user,
			@Valid @RequestBody AddVehicleRequest request) {
		Vehicle newEntity = vehicleService.addVehicle(request, user.getUserEntity());
		return ResponseEntity.ok(new CreateResponse<Vehicle>("Vehicle Vehicle ", newEntity));
	}

	@PutMapping("/{id}")
	public ResponseEntity<SimpleResponse> update(@PathVariable(value = "id") Long id,
			@Valid @RequestBody UpdateVehicleRequest request) {
		request.setId(id);
		vehicleService.updateVehicle(request);

		return ResponseEntity.ok(new SimpleResponse("Vehicle Added"));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<SimpleResponse> delete(@PathVariable(value = "id") Long id) {

		vehicleService.deleteVehicle(id);

		return ResponseEntity.ok(new SimpleResponse("Vehicle Deleted"));
	}
}