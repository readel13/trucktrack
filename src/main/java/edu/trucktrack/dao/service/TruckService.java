package edu.trucktrack.dao.service;

import edu.trucktrack.api.dto.TruckDTO;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.dao.repository.jooq.TruckJooqRepository;
import edu.trucktrack.dao.repository.jpa.TruckJpaRepository;
import edu.trucktrack.dao.entity.TruckEntity;
import edu.trucktrack.mapper.TruckMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TruckService {
    private final TruckMapper mapper;

    private final CompanyService companyService;

    private final TruckJpaRepository truckJpaRepository;

    private final TruckJooqRepository truckJooqRepository;

    //TODO: fix it for supporting update or create new method
    public TruckDTO save(TruckDTO truckDTO) {
        if (truckJooqRepository.exist(truckDTO)) {
            throw new IllegalArgumentException("Something wrong with creation truck");
        }

        TruckEntity entity = mapper.toEntity(truckDTO);
        var company = companyService.getById(truckDTO.getCompanyId());

        entity.setCompany(company);
        return mapper.toDTO(truckJpaRepository.save(entity));
    }

    public void deleteById(Long id) {
        truckJpaRepository.deleteById(id);
    }


    // TODO: pagination
    public List<TruckDTO> getAll(SearchCriteriaRequest criteriaRequest) {
        return truckJooqRepository.getAll(criteriaRequest);
    }


    public List<TruckDTO> getAllAvailableForEmployee(Long employeeId) {
        return truckJooqRepository.getAllAvailableForEmployee(employeeId);
    }
}
