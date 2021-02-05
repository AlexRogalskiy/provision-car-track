package com.provision.cartrack.actions.readings;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadingRepository extends JpaRepository<Reading, Long> {

	Optional<Reading> findByExternalReference(String externalReference);

	Boolean existsByExternalReference(String externalReference);

}