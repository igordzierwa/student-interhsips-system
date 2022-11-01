package agh.studentInternshipSupportSystem.StudentGroups;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentGroupRepository extends JpaRepository<StudentGroup, Long> {
    StudentGroup findStudentGroupByStudentGroupName(String name);
    StudentGroup findStudentGroupById(Long id);
}
