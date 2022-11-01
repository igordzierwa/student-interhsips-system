package agh.studentInternshipSupportSystem.UniversitySupervisors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversitySupervisorRepository extends JpaRepository<UniversitySupervisor, Long> {
    UniversitySupervisor findUniversitySupervisorByEmail(String email);
}
