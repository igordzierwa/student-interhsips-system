package agh.studentInternshipSupportSystem.Companies;

import agh.studentInternshipSupportSystem.Companies.RequestBody.AddCompany;

public interface CompanyService {
    Company findById(Long id);
    void save(Company company);
    void edit(AddCompany company, Long id);
}
