package agh.studentInternshipSupportSystem.VerificationToken;

import agh.studentInternshipSupportSystem.CompanySupervisors.CompanySupervisor;
import agh.studentInternshipSupportSystem.Internships.Internship;
import agh.studentInternshipSupportSystem.UniversitySupervisors.UniversitySupervisor;
import agh.studentInternshipSupportSystem.User.User;

public interface TokenGenerator {
    String generateAuthToken(User user);
    String generateAuthToken(Internship internship, UniversitySupervisor universitySupervisor);
    String generateAuthToken(Internship internship, CompanySupervisor universitySupervisor);

}
