package agh.studentInternshipSupportSystem.Departments;

import org.springframework.stereotype.Service;

@Service
public interface DepartmentService{
    Department findById(Long id);
    void save(Department department);
    void edit(Department department, Long id);
}
