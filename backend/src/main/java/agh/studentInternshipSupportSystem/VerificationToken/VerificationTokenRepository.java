package agh.studentInternshipSupportSystem.VerificationToken;

import agh.studentInternshipSupportSystem.CompanySupervisors.CompanySupervisor;
import agh.studentInternshipSupportSystem.Internships.Internship;
import agh.studentInternshipSupportSystem.UniversitySupervisors.UniversitySupervisor;
import agh.studentInternshipSupportSystem.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;


public interface VerificationTokenRepository<T extends VerificationToken, E extends Serializable> extends JpaRepository<T, E>{


}

