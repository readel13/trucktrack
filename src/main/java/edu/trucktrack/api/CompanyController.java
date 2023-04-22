package edu.trucktrack.api;

import edu.trucktrack.entity.Company;
import edu.trucktrack.record.CompanyEntityRecord;
import edu.trucktrack.repository.CompanyJpaRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyJpaRepository companyJpaRepository;

    private final DSLContext context;

    @PostMapping
    public Company create(@RequestBody Company company) {
        return companyJpaRepository.save(company);
    }

    @PostMapping("/jooq")
    public void createJooq(@RequestBody Company body) {
        var company = new edu.trucktrack.jooq.tables.Company("company");


        context.insertInto(company)
                .set(company.NAME, body.getName())
                .set(company.EMAIL, body.getEmail())
                .set(company.DESCRIPTION, body.getDescription())
                .set(company.URL, body.getUrl())
                .set(company.ADDRESS, body.getUrl())
                .set(company.ZIPCODE, body.getZipcode())
                .set(company.CREATED_AT, LocalDateTime.now())
                .execute();
    }

    @GetMapping("/all/jooq")
    public List<CompanyEntityRecord> fetchAllUsingJooq() {
        var company = new edu.trucktrack.jooq.tables.Company("company");

        return context.select(company.asterisk())
                .from(company)
                .fetchInto(CompanyEntityRecord.class);
    }
}
