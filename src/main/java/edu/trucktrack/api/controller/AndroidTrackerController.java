package edu.trucktrack.api.controller;

import edu.trucktrack.api.dto.AndroidTrackerLocationData;
import edu.trucktrack.util.MapUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tracker")
@RequiredArgsConstructor
public class AndroidTrackerController {

	private final MapUpdater mapUpdater;

	@GetMapping
	public ResponseEntity<?> checkLogin() {
		return ResponseEntity.ok("Auth complete");
	}

	@PostMapping
	public ResponseEntity<?> updateCoordinates(@RequestBody AndroidTrackerLocationData locationData) {
		mapUpdater.addLocationData(locationData);
		return ResponseEntity.ok("updating complete");
	}
}
