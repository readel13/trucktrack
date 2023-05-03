package edu.trucktrack.service;

import edu.trucktrack.api.dto.TruckDTO;
import edu.trucktrack.entity.TruckEntity;
import edu.trucktrack.mapper.TruckMapper;
import edu.trucktrack.repository.jooq.TruckJooqRepository;
import edu.trucktrack.repository.jpa.TruckJpaRepository;
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

    public TruckDTO save(TruckDTO truckDTO) {
        if (!truckJooqRepository.validateForSave(truckDTO)) {
            throw new IllegalArgumentException("Something wrong with creation truck");
        }

        TruckEntity entity = mapper.toEntity(truckDTO);
        var company = companyService.getById(Long.valueOf(truckDTO.getCompanyId()));

        entity.setCompany(company);
        return mapper.toDTO(truckJpaRepository.save(entity));
    }

    // TODO: rewrite it using JOOQ for supporting pagination
    // TODO: add property isAvailable and badge in front-end: availble - when all trips with this truck closed or not exist
    public List<TruckDTO> getAll() {
        return truckJpaRepository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }
}
