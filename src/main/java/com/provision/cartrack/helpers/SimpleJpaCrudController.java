package com.provision.cartrack.helpers;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

public abstract class SimpleJpaCrudController<T, ID extends Serializable> {

	protected JpaRepository<T, ID> repo;

	public SimpleJpaCrudController(JpaRepository<T, ID> repo) {
		this.repo = repo;
	}

	@GetMapping
	public @ResponseBody List<T> listAll() {
		return this.repo.findAll();
	}

	@PostMapping
	public @ResponseBody ResponseEntity<CreateResponse<T>> create(@Valid @RequestBody T json) {
		T created = this.repo.save(json);
		return ResponseEntity.ok().body(new CreateResponse<T>("Created Successfully", created));
	}

	@GetMapping("/{id}")
	public @ResponseBody ResponseEntity<T> get(@PathVariable ID id) {
		return ResponseEntity.of(this.repo.findById(id));
	}

	@PutMapping(value = "/{id}")
	public @ResponseBody ResponseEntity<SimpleResponse> update(@PathVariable ID id, @Valid @RequestBody T json) {

		Optional<T> entity = this.repo.findById(id);

		if (entity.isEmpty()) {
			return new ResponseEntity<SimpleResponse>(HttpStatus.NOT_FOUND);
		}

		T x = entity.get();
		try {
			BeanUtils.copyProperties(json, x);
			Method setid = BeanUtils.findDeclaredMethodWithMinimalParameters(json.getClass(), "setId");
			setid.invoke(x, id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		this.repo.save(x);

		return ResponseEntity.ok().body(new SimpleResponse("Updated Successfully"));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<SimpleResponse> delete(@PathVariable ID id) {

		T value = this.repo.findById(id).get();
		if (value == null)
			return new ResponseEntity<SimpleResponse>(HttpStatus.NOT_FOUND);

		this.repo.delete(value);
		return ResponseEntity.ok().body(new SimpleResponse("Deleted Successfully"));
	}
}