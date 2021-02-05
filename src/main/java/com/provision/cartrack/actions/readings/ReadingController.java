package com.provision.cartrack.actions.readings;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.provision.cartrack.helpers.CreateResponse;
import com.provision.cartrack.helpers.SimpleResponse;
import com.provision.cartrack.security.UserPrinciple;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/readings")
public class ReadingController {

	@Autowired
	ReadingService readingService;

	@Autowired
	ReadingRepository readingRepository;

	@PostMapping
	public ResponseEntity<CreateResponse<Reading>> addReading(@Valid @RequestBody ReadingRequest request,
			@AuthenticationPrincipal UserPrinciple user) {

		Reading x = readingService.addReading(request, user.getUserEntity());

		return ResponseEntity.ok(new CreateResponse<Reading>("Reading Added", x));
	}

	@PutMapping
	public ResponseEntity<CreateResponse<Reading>> updateReading(@Valid @RequestBody ReadingRequest request,
			@AuthenticationPrincipal UserPrinciple user) {

		Reading x = readingService.updateReading(request, user.getUserEntity());

		return ResponseEntity.ok(new CreateResponse<Reading>("Reading Updated", x));
	}

	@PostMapping("/bulk-import")
	public ResponseEntity<SimpleResponse> bulkImport(@RequestBody String data,
			@AuthenticationPrincipal UserPrinciple user) {
		ArrayList<String> lines = new ArrayList<String>();
		try (Scanner scan = new Scanner(data)) {
			while (scan.hasNextLine()) {
				lines.add(scan.nextLine());
			}
		}
		readingService.bulkImport(lines, user.getUserEntity());
		return ResponseEntity.ok(new SimpleResponse("Bulk Import Finished " + lines.size() + " lines"));
	}

	@GetMapping
	public @ResponseBody List<Reading> listAll() {
		return this.readingRepository.findAll();
	}

	@GetMapping("/{id}")
	public @ResponseBody ResponseEntity<Reading> get(@PathVariable Long id) {
		return ResponseEntity.of(readingRepository.findById(id));
	}

	@GetMapping("/by-external-ref/{ref}")
	public @ResponseBody ResponseEntity<Reading> get(@PathVariable String ref) {
		return ResponseEntity.of(readingRepository.findByExternalReference(ref));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<SimpleResponse> delete(@PathVariable(value = "id") Long id,
			@AuthenticationPrincipal UserPrinciple user) {

		readingService.deleteReading(id, user.getUserEntity());

		return ResponseEntity.ok(new SimpleResponse("Reading Deleted"));
	}

}