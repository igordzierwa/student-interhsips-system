package agh.studentInternshipSupportSystem.Departments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>{
    Department findAllByNameAndShortname(String name, String short_name);
    boolean existsByNameAndShortname(String name, String short_name);
}

