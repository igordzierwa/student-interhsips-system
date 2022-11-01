package agh.studentInternshipSupportSystem.VerificationToken;
import agh.studentInternshipSupportSystem.CompanySupervisors.CompanySupervisor;
import agh.studentInternshipSupportSystem.Internships.Internship;
import agh.studentInternshipSupportSystem.UniversitySupervisors.UniversitySupervisor;
import agh.studentInternshipSupportSystem.User.User;

import java.util.List;


public interface VerificationTokenInternshipCSRepository extends VerificationTokenRepository<VerificationTokenInternshipCS, Long>{
   // T findVerificationTokenByInternshipAndUniversitySupervisor(Internship internship, UniversitySupervisor universitySupervisor);
    VerificationTokenInternshipCS findVerificationTokenByToken(String token);
    VerificationTokenInternshipCS findVerificationTokenByInternshipAndCompanySupervisor(Internship internship, CompanySupervisor companySupervisor);
    List<VerificationTokenInternshipCS> findVerificationTokenInternshipCSByCompanySupervisor_Username(String username);
}

