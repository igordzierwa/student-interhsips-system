package agh.studentInternshipSupportSystem.CompanySupervisors;

import agh.studentInternshipSupportSystem.Companies.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanySupervisorRepository extends JpaRepository<CompanySupervisor, Long> {
    CompanySupervisor findCompanySupervisorByEmail(String email);
    List<CompanySupervisor> findCompanySupervisorsByCompany(Company company);
    CompanySupervisor findCompanySupervisorById(Long id);
}
