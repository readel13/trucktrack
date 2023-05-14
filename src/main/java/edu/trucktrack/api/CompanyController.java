package edu.trucktrack.api;

import edu.trucktrack.api.dto.CompanyDTO;
import edu.trucktrack.api.dto.CompanyEmployeeDTO;
import edu.trucktrack.mapper.CompanyMapper;
import edu.trucktrack.mapper.EmployeeMapper;
import edu.trucktrack.dao.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyMapper mapper;

    private final CompanyService service;

    private final EmployeeMapper employeeMapper;

    @PostMapping
    public CompanyDTO create(@Validated @RequestBody CompanyEmployeeDTO companyEmployeeDTO) {
        var company = mapper.toEntity(companyEmployeeDTO.getCompanyDTO());
        var employee = employeeMapper.toEntity(companyEmployeeDTO.getEmployeeDTO());

        return mapper.toDTO(service.save(company, employee));
    }

    @PutMapping("/{id}")
    public CompanyDTO update(@Validated @RequestBody CompanyDTO companyDTO, @PathVariable Long id) {
        companyDTO.setId(id);

        return mapper.toDTO(service.update(mapper.toEntity(companyDTO)));
    }

    @GetMapping("/{id}")
    public CompanyDTO getById(@PathVariable Long id) {
        return mapper.toDTO(service.getById(id));
    }
}
