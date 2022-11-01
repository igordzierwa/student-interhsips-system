package agh.studentInternshipSupportSystem.Departments;

import agh.studentInternshipSupportSystem.Exceptions.DepartmentNotExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Department findById(Long id) {
        Department existDepartment = departmentRepository
                .findById(id)
                .orElseThrow(() -> new DepartmentNotExistException("Department Not Exist"));
        return existDepartment;
    }

    @Override
    public void save(Department department) {
        departmentRepository.save(department);
    }

    @Override
    public void edit(Department department, Long id) {
        Department existDepartment = findById(id);

        existDepartment.setName(department.getName());
        existDepartment.setShortname(department.getShortname());
        departmentRepository.save(existDepartment);
    }
}
