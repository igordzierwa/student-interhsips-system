package agh.studentInternshipSupportSystem.Companies;

import agh.studentInternshipSupportSystem.Companies.RequestBody.AddCompany;
import agh.studentInternshipSupportSystem.Exceptions.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public Company findById(Long id) {
        Company existCompany = companyRepository
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Company Not Exist"));
        return existCompany;
    }

    @Override
    public void save(Company company) {
        companyRepository.save(company);
    }

    @Override
    public void edit(AddCompany addCompany, Long id) {
        Company existCompany = findById(id);

        existCompany.setCompanyName(addCompany.getCompanyName());
        existCompany.setNip(addCompany.getNip());
        existCompany.setRegon(addCompany.getRegon());
        existCompany.setAddress(addCompany.getAddress());

        companyRepository.save(existCompany);
    }
}
