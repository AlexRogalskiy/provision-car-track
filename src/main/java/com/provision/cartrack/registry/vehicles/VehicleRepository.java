package com.provision.cartrack.registry.vehicles;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	Optional<Vehicle> findBySerialNumber(String serialNumber);
}