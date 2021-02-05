package com.provision.cartrack.registry.models;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.provision.cartrack.registry.Make;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long>, ModelRepositoryCustom {
	Optional<Model> findByNameAndYearAndMake(String name, Integer year, Make make);
}