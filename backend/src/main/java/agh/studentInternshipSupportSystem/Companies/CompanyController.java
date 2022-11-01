package agh.studentInternshipSupportSystem.Companies;

import agh.studentInternshipSupportSystem.Companies.RequestBody.AddCompany;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
public class CompanyController {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyService companyService;

    @PreAuthorize("hasAnyAuthority('admin', 'student', 'university_supervisor')")
    @PostMapping("/companies")
    public ResponseEntity<Object> addCompany(@RequestBody @Valid AddCompany addCompany) {
        Company company = new Company(addCompany);
        companyService.save(company);
        return ResponseEntity.ok("\"status\": \"company added\"");
    }

    @PreAuthorize("hasAnyAuthority('admin', 'student', 'university_supervisor')")
    @PostMapping("/companies/{id}")
    public ResponseEntity<Object> editCompany(@RequestBody @Valid AddCompany addCompany, @PathVariable Long id) {
        companyService.edit(addCompany, id);
        return ResponseEntity.ok("\"status\": \"company updated\"");
    }

    @PreAuthorize("hasAnyAuthority('admin', 'student', 'university_supervisor', 'company_supervisor')")
    @GetMapping("/companies")
    public ResponseEntity<Object> getAllCompanies() {
        return ResponseEntity.ok(companyRepository.findAll());
    }

    @PreAuthorize("hasAnyAuthority('admin', 'student', 'university_supervisor', 'company_supervisor')")
    @GetMapping("/companies/{id}")
    public ResponseEntity<Object> getSpecificCompany(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.findById(id));
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Object> deleteSpecificCompany(@PathVariable Long id) {
        companyRepository.delete(companyService.findById(id));
        return ResponseEntity.ok("\"status\": \"company deleted\"");
    }
}
