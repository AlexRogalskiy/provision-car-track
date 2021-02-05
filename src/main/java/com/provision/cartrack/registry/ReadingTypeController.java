package com.provision.cartrack.registry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.provision.cartrack.helpers.SimpleJpaCrudController;

@Controller
@RequestMapping("/api/v1/readingtypes")
public class ReadingTypeController extends SimpleJpaCrudController<ReadingType, Long> {

	@Autowired
	public ReadingTypeController(ReadingTypeRepository repo) {
		super(repo);
	}

	@GetMapping("/by-name/{name}")
	public @ResponseBody ResponseEntity<ReadingType> get(@PathVariable String name) {
		return ResponseEntity.of(((ReadingTypeRepository) this.repo).findByName(name));
	}
}