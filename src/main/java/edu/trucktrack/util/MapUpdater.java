package edu.trucktrack.util;

import edu.trucktrack.api.dto.AndroidTrackerLocationData;
import edu.trucktrack.api.dto.DriverDataForMapDTO;
import edu.trucktrack.service.MapService;
import jakarta.inject.Singleton;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Singleton
@Component
public class MapUpdater {

	private final MapService mapService;

	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock rLock = lock.readLock();
	private final Lock wLock = lock.writeLock();

	private final Map<String, DriverDataForMapDTO> driverDataForMapDTOMap = new HashMap<>();
	private final Map<String, AndroidTrackerLocationData> locationDataMap = new HashMap<>();

	public MapUpdater(MapService mapService) {
		this.mapService = mapService;
		this.addLocationData(new AndroidTrackerLocationData("vasia@mail.com", 48.15844527759325, 25.726506569491583, 31.1));

	}

	public List<DriverDataForMapDTO> getDriverData(Long companyId) {
		rLock.lock();
		try {
			return driverDataForMapDTOMap.values().stream().filter(item -> companyId.equals(item.getDriverDataForMap().getCompanyId())).toList();
		} finally {
			rLock.unlock();
		}
	}

	public void addLocationData(AndroidTrackerLocationData androidTrackerLocationData) {
		locationDataMap.put(androidTrackerLocationData.getEmail(), androidTrackerLocationData);
	}

	public void updateDriverData (List<DriverDataForMapDTO> data) {
		data.forEach(item -> {
			if (driverDataForMapDTOMap.get(item.getDriverDataForMap().getEmail()) != null) {
				driverDataForMapDTOMap.get(item.getDriverDataForMap().getEmail()).setDriverDataForMap(item.getDriverDataForMap());
				driverDataForMapDTOMap.get(item.getDriverDataForMap().getEmail()).setLocationData(item.getLocationData());
			} else {
				driverDataForMapDTOMap.put(item.getDriverDataForMap().getEmail(), item);
			}
		});
	}

	@Bean
	@Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
	public void updateDriverDataByTime() {
		updateDriverData(mapService.getDriverData());
	}

	@Bean
	@Scheduled(fixedRate = 2, timeUnit = TimeUnit.SECONDS)
	public void updateLocationDataByTime() {
		wLock.lock();
		try {
			if (!locationDataMap.isEmpty()) {
				locationDataMap.forEach((key, value) -> Optional.ofNullable(driverDataForMapDTOMap.get(key)).ifPresent(v -> v.setLocationData(value)));
			}
		} finally {
			wLock.unlock();
		}
	}

	@Bean
	@Scheduled(fixedDelay = 2, timeUnit = TimeUnit.SECONDS)
	public void updateCoordinates() {
		locationDataMap.forEach((key, value) -> value.setLongitude(value.getLongitude() + 0.01));
	}
}
