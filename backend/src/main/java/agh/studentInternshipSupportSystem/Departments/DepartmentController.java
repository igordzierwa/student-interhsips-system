package agh.studentInternshipSupportSystem.Departments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/departments")
    public ResponseEntity<Object> addDepartment(@RequestBody @Valid Department department) {
        departmentService.save(department);
        return ResponseEntity.ok("\"status\": \"department added\"");
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/departments/{id}")
    public ResponseEntity<Object> editDepartment(@RequestBody @Valid Department department, @PathVariable Long id) {
        departmentService.edit(department, id);
        return ResponseEntity.ok("\"status\": \"department updated\"");
    }

    @PreAuthorize("hasAnyAuthority('admin', 'company_supervisor', 'university_supervisor', 'student')")
    @GetMapping("/departments")
    public ResponseEntity<Object> listDepartments() {
        return ResponseEntity.ok().body(departmentRepository.findAll());
    }

    @PreAuthorize("hasAnyAuthority('admin', 'company_supervisor', 'university_supervisor', 'student')")
    @GetMapping("/departments/{id}")
    public ResponseEntity<Object> getDepartment(@PathVariable Long id) {
        return ResponseEntity.ok().body(departmentRepository.findById(id));
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/departments/{id}")
    ResponseEntity<Object> deleteSpecificDepartment(@PathVariable Long id) {
        departmentRepository.delete(departmentService.findById(id));
        return ResponseEntity.ok("\"status\": \"department deleted\"");
    }
}
