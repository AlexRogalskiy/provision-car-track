package com.provision.cartrack.registry.models;

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
import com.provision.cartrack.helpers.Pagination;
import com.provision.cartrack.helpers.SimpleResponse;
import com.provision.cartrack.security.UserPrinciple;

@Controller
@RequestMapping("/api/v1/models")
public class ModelController {

	@Autowired
	ModelService modelService;

	@Autowired
	ModelRepository modelRepository;

	@PostMapping("/search")
	public @ResponseBody ResponseEntity<Pagination<Model>> search(@Valid @RequestBody Pagination<Model> request) {
		Pagination<Model> result = this.modelRepository.search(request);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping
	public @ResponseBody List<Model> listAll() {
		return this.modelRepository.findAll();
	}

	@GetMapping("/{id}")
	public @ResponseBody ResponseEntity<Model> get(@PathVariable Long id) {
		Model value = this.modelRepository.findById(id).get();
		if (value == null)
			return new ResponseEntity<Model>(HttpStatus.NOT_FOUND);
		return ResponseEntity.ok().body(value);
	}

	@PostMapping
	public ResponseEntity<CreateResponse<Model>> create(@AuthenticationPrincipal UserPrinciple user,
			@Valid @RequestBody AddModelRequest request) {
		Model newModel = modelService.addModel(request, user.getUserEntity());
		return ResponseEntity.ok(new CreateResponse<Model>("Model Added", newModel));
	}

	@PutMapping("/{id}")
	public ResponseEntity<SimpleResponse> update(@PathVariable(value = "id") Long id,
			@Valid @RequestBody UpdateModelRequest request) {
		request.setId(id);
		modelService.updateModel(request);

		return ResponseEntity.ok(new SimpleResponse("Model Added"));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<SimpleResponse> delete(@PathVariable(value = "id") Long id) {

		modelService.deleteModel(id);

		return ResponseEntity.ok(new SimpleResponse("Model Deleted"));
	}

}