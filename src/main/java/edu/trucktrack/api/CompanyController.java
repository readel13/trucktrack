package edu.trucktrack.api;

import edu.trucktrack.entity.Company;
import edu.trucktrack.repository.CompanyJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyJpaRepository companyJpaRepository;

    @PostMapping
    public Company create(@RequestBody Company company) {
        return companyJpaRepository.save(company);
    }
}
