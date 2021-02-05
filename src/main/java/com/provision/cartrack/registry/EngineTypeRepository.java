package com.provision.cartrack.registry;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EngineTypeRepository extends JpaRepository<EngineType, Long> {
	Optional<EngineType> findByName(String name);
}