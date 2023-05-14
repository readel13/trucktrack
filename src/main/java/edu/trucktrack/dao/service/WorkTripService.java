package edu.trucktrack.dao.service;

import edu.trucktrack.api.dto.WorkTripDTO;
import edu.trucktrack.api.request.FilterBy;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.dao.repository.jooq.WorkTripJooqRepo;
import edu.trucktrack.dao.repository.jooq.WorkTripRecordEntity;
import edu.trucktrack.dao.repository.jpa.WorkTripJpaRepository;
import edu.trucktrack.mapper.WorkTripMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkTripService {

    private final WorkTripMapper mapper;

    private final WorkTripJooqRepo workTripJooqRepo;

    private final WorkTripJpaRepository workTripJpaRepository;

    @Transactional
    public WorkTripDTO saveOrUpdate(WorkTripDTO workTripDTO) {
        var saved = workTripJpaRepository.save(mapper.toEntity(workTripDTO));

        return mapper.toDTO(saved);
    }

    public WorkTripDTO getById(Long id) {
        return get(SearchCriteriaRequest.builder()
                .filterBy(FilterBy.builder().tripId(id).build())
                .build())
                .stream().findFirst().orElse(null);
    }

    //TODO: implement pagination
    public List<WorkTripDTO> get(SearchCriteriaRequest criteria) {
        List<WorkTripRecordEntity> workTrips = workTripJooqRepo.getWorkTrips(criteria);
        return workTrips.stream().map(mapper::toDTO).toList();
    }

    public void delete(Long id) {
        workTripJpaRepository.deleteById(id);
    }
}
