package agh.studentInternshipSupportSystem.Students;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;


@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findStudentByEmail(String email);
    Student findStudentByUsername(String username);
    Student findStudentById(Long id);
    List<Student> findAllByCreatedDateAfterAndCreatedDateBefore(Date after, Date before);
    @Query(value = "SELECT departments.*, department_id, COUNT(*)  as total FROM `students` " +
            "LEFT JOIN departments ON departments.id = students.department_id " +
            "GROUP BY department_id " +
            "ORDER BY total DESC LIMIT 10 ", nativeQuery = true)
    List<IPopularDepartment> getPopularDepartments();
}


