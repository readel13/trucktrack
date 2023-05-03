package edu.trucktrack.service;

import edu.trucktrack.api.dto.WorkTripDTO;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.mapper.WorkTripMapper;
import edu.trucktrack.repository.jooq.WorkTripJooqRepo;
import edu.trucktrack.repository.jooq.WorkTripRecordEntity;
import edu.trucktrack.repository.jpa.WorkTripJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkTripService {

    private final WorkTripMapper mapper;

    private final WorkTripJooqRepo workTripJooqRepo;

    private final WorkTripJpaRepository workTripJpaRepository;

    public WorkTripDTO save(WorkTripDTO workTripDTO) {
        var saved = workTripJpaRepository.save(mapper.toEntity(workTripDTO));

        return mapper.toDTO(saved);
    }

    //TODO: implement pagination
    public List<WorkTripDTO> get(SearchCriteriaRequest criteria) {
        List<WorkTripRecordEntity> workTrips = workTripJooqRepo.getWorkTrips(criteria);
        return workTrips.stream().map(mapper::toDTO).toList();
    }
}
