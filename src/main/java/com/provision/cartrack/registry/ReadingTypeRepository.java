package com.provision.cartrack.registry;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadingTypeRepository extends JpaRepository<ReadingType, Long> {
	Optional<ReadingType> findByName(String name);
}