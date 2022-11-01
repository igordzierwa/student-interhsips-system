package agh.studentInternshipSupportSystem.Internships;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternshipRepository extends JpaRepository<Internship, Long> {
    Internship findByStudentUsername(String username);
}
