package com.provision.cartrack.actions.readings;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.provision.cartrack.actions.Event;
import com.provision.cartrack.actions.Event.EventType;
import com.provision.cartrack.actions.EventRepository;
import com.provision.cartrack.helpers.JSONable;
import com.provision.cartrack.registry.ReadingType;
import com.provision.cartrack.registry.ReadingTypeRepository;
import com.provision.cartrack.registry.vehicles.Vehicle;
import com.provision.cartrack.registry.vehicles.VehicleRepository;
import com.provision.cartrack.security.User;

@Service
public class ReadingService {

	@Autowired
	private ReadingRepository readingRepository;

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private ReadingTypeRepository readingTypeRepository;

	@Autowired
	private EventRepository eventRepository;

	public Reading addReading(ReadingRequest request, User user) {
		Vehicle vehicle = vehicleRepository.findBySerialNumber(request.getVehicleSerialNumber())
				.orElseThrow(() -> new RuntimeException(
						"Fail! -> Cause: Vehicle " + request.getVehicleSerialNumber() + " not found."));

		ReadingType readingType = readingTypeRepository.findByName(request.getReadingType()).orElseThrow(
				() -> new RuntimeException("Fail! -> Cause: ReadingType " + request.getReadingType() + " not found."));

		Reading x = new Reading();
		Optional<Reading> exists = readingRepository.findByExternalReference(request.getExternalReference());
		if (exists.isPresent()) {
			throw new RuntimeException(
					"Fail! -> Cause: External Reference " + request.getExternalReference() + " already exists.");
		}

		x.setVehicle(vehicle);
		x.setReadingType(readingType);
		x.setExecutionDate(request.getExecutionDate());
		x.setOdometerValue(request.getOdometerValue());
		x.setReadingValue(request.getReadingValue());
		x.setExternalReference(request.getExternalReference());
		x.auditCreation(user);

		x.invariantsValidation();

		x = readingRepository.save(x);

		eventRepository.save(new Event(user, EventType.READING_ADDED, request));

		return x;
	}

	public Reading updateReading(ReadingRequest request, User user) {
		Reading reading = readingRepository.findByExternalReference(request.getExternalReference())
				.orElseThrow(() -> new RuntimeException(
						"Fail! -> Cause: Reading " + request.getExternalReference() + " not found."));

		Vehicle vehicle = vehicleRepository.findBySerialNumber(request.getVehicleSerialNumber())
				.orElseThrow(() -> new RuntimeException(
						"Fail! -> Cause: Vehicle " + request.getVehicleSerialNumber() + " not found."));

		ReadingType readingType = readingTypeRepository.findByName(request.getReadingType()).orElseThrow(
				() -> new RuntimeException("Fail! -> Cause: ReadingType " + request.getReadingType() + " not found."));

		if (request.getExecutionDate().after(new Date()))
			throw new IllegalArgumentException("Execution Date cannot be in the future.");

		reading.setVehicle(vehicle);
		reading.setReadingType(readingType);
		reading.setExecutionDate(request.getExecutionDate());
		reading.setOdometerValue(request.getOdometerValue());
		reading.setReadingValue(request.getReadingValue());
		reading.auditCreation(user);

		reading.invariantsValidation();

		reading = readingRepository.save(reading);

		eventRepository.save(new Event(user, EventType.READING_UPDATED, request));

		return reading;
	}

	/**
	 * Imports a batch of readings in the following format using the PIPE separator
	 * 
	 * // external ref | serial number | date | type | odometer | reading value
	 * 
	 * IF the reading already exists it will be updated
	 *
	 **/
	public void bulkImport(List<String> readingsLines, User user) {
		readingsLines.forEach(line -> {
			String[] values = line.split("\\|");
			int colIdx = 0;
			ReadingRequest rr = new ReadingRequest();
			rr.setExternalReference((values[colIdx++].trim()));
			rr.setVehicleSerialNumber(values[colIdx++].trim());
			String dateval = values[colIdx++].trim();
			try {
				rr.setExecutionDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateval));
			} catch (ParseException e) {
				throw new RuntimeException("Bad Date " + dateval);
			}
			rr.setReadingType(values[colIdx++].trim());
			rr.setOdometerValue(Integer.parseInt(values[colIdx++].trim()));
			rr.setReadingValue(new BigDecimal(values[colIdx++].trim()));

			Boolean exists = readingRepository.existsByExternalReference(rr.getExternalReference());
			if (!exists) {
				addReading(rr, user);
			} else {
				updateReading(rr, user);
			}
		});
	}

	public void deleteReading(Long id, User user) {
		Reading x = readingRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: Reading " + id + " not found."));

		readingRepository.delete(x);

		DeleteRequest dr = new DeleteRequest();
		dr.readingId = id;
		eventRepository.save(new Event(user, EventType.READING_REMOVED, dr));
	}

	private static class DeleteRequest implements JSONable {
		@SuppressWarnings("unused")
		public Long readingId;

		public String toJSON() {
			try {
				return new ObjectMapper().writeValueAsString(this);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
