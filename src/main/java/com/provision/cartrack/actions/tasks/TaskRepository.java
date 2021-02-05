package com.provision.cartrack.actions.tasks;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	Optional<Task> findByExternalReference(String externalReference);

	Boolean existsByExternalReference(String externalReference);

}