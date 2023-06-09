package edu.trucktrack.api.controller;

import edu.trucktrack.api.dto.AndroidTrackerLocationData;
import edu.trucktrack.dao.entity.WorkTripSalaryHistoryEntity;
import edu.trucktrack.dao.service.SalaryService;
import edu.trucktrack.util.MapUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/tracker")
@RequiredArgsConstructor
public class AndroidTrackerController {

	private final MapUpdater mapUpdater;
	private final SalaryService salaryService;

	@RequestMapping(method = RequestMethod.POST, path = "/auth")
	public ResponseEntity<?> checkLogin(Authentication authentication) {
		return ResponseEntity.ok("Auth complete");
	}

	@PostMapping("/coordinates")
	public ResponseEntity<?> updateCoordinates(Authentication authentication, @RequestBody AndroidTrackerLocationData locationData) {
		locationData.setEmail(authentication.getName());
		mapUpdater.addLocationData(locationData);
		return ResponseEntity.ok("updating complete");
	}

	@GetMapping("/distance")
	public ResponseEntity<?> saveDailyDistance(Authentication authentication, @RequestParam Integer distance) {
		WorkTripSalaryHistoryEntity entity = salaryService.saveDailyDistance(authentication.getName(), distance);
		if (entity != null) {
			return ResponseEntity.ok("Save successful");
		}
		return ResponseEntity.status(500).build();
	}
}
