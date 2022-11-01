package agh.studentInternshipSupportSystem.StudentGroups;

import agh.studentInternshipSupportSystem.StudentGroups.RequestBody.AddStudentGroup;
import agh.studentInternshipSupportSystem.StudentGroups.RequestBody.AddStudentToGroup;
import agh.studentInternshipSupportSystem.Students.Student;
import agh.studentInternshipSupportSystem.Students.StudentRepository;
import agh.studentInternshipSupportSystem.UniversitySupervisors.UniversitySupervisor;
import agh.studentInternshipSupportSystem.UniversitySupervisors.UniversitySupervisorRepository;
import agh.studentInternshipSupportSystem.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Set;

@RestController
public class StudentGroupController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentGroupRepository studentGroupRepository;

    @Autowired
    private UniversitySupervisorRepository universitySupervisorRepository;

    @Autowired
    private StudentGroupService studentGroupService;

    @Autowired
    private StudentRepository studentRepository;

    @PreAuthorize("hasAnyAuthority('admin', 'university_supervisor')")
    @PostMapping("/studentGroups")
    public ResponseEntity<Object> createStudentGroup(@RequestBody @Valid AddStudentGroup addStudentGroup) {
        UniversitySupervisor universitySupervisor = universitySupervisorRepository
                .findById(addStudentGroup.getUniversitySupervisorId()).get();
        StudentGroup studentGroup = new StudentGroup(addStudentGroup.getStudentGroupName(), universitySupervisor);
        studentGroupService.save(studentGroup);

        return ResponseEntity.ok("\"status\": \"student group created\"");
    }

    @PreAuthorize("hasAnyAuthority('admin', 'university_supervisor')")
    @PostMapping("/studentGroups/{id]")
    public ResponseEntity<Object> editStudentGroup(@RequestBody @Valid AddStudentGroup addStudentGroup,
                                                   @PathVariable Long id) {
        studentGroupService.edit(addStudentGroup, id);

        return ResponseEntity.ok("\"status\": \"student group updated\"");
    }

    @PreAuthorize("hasAnyAuthority('admin', 'university_supervisor')")
    @PostMapping("/studentGroup/{id}/add/{username}")
    public ResponseEntity<Object> addStudent(@PathVariable Long id, @PathVariable String username) {
        Student student = (Student) userRepository.findByUsername(username);
        StudentGroup studentGroup = studentGroupService.findById(id);

        studentGroup.getStudents().add(student);
        studentGroupService.save(studentGroup);

        return ResponseEntity.ok("\"status\": \"student added\"");
    }

    @PreAuthorize("hasAnyAuthority('admin', 'university_supervisor')")
    @DeleteMapping("/studentGroup/{id}/remove/{username")
    public ResponseEntity<Object> removeStudent(@PathVariable Long id, @PathVariable String username) {
        Student student = (Student) userRepository.findByUsername(username);
        StudentGroup studentGroup = studentGroupService.findById(id);

        studentGroup.getStudents().remove(student);
        studentGroupService.save(studentGroup);

        return ResponseEntity.ok("\"status\": \"student removed\"");
    }

    @PreAuthorize("hasAnyAuthority('admin', 'university_supervisor', 'company_supervisor', 'student')")
    @GetMapping("/studentGroups")
    public ResponseEntity<Object> getAllStudentGroups() {
        return ResponseEntity.ok(studentGroupRepository.findAll());
    }

    @PreAuthorize("hasAnyAuthority('admin', 'university_supervisor', 'company_supervisor', 'student')")
    @GetMapping("/studentGroups/{id}")
    public ResponseEntity<Object> getSpecificStudentGroup(@PathVariable Long id) {
        return ResponseEntity.ok(studentGroupService.findById(id));
    }

    @PreAuthorize("hasAnyAuthority('admin', 'university_supervisor')")
    @DeleteMapping("/studentGroups/{id}")
    public ResponseEntity<Object> deleteSpecificStudentGroup(@PathVariable Long id) {
        studentGroupRepository.delete(studentGroupService.findById(id));
        return ResponseEntity.ok("\"status\": \"student group deleted\"");
    }

    @PreAuthorize("hasAnyAuthority('admin', 'university_supervisor')")
    @GetMapping("/studentGroups/check/{name}")
    public ResponseEntity<Object> checkGroupNameAvailability(@PathVariable String name){
        StudentGroup studentGroup = studentGroupRepository.findStudentGroupByStudentGroupName(name);
        if(studentGroup == null){
            return ResponseEntity.ok("\"true\"");
        }
        return ResponseEntity.ok("\"false\"");
    }

    @PreAuthorize("hasAnyAuthority('admin', 'university_supervisor')")
    @PostMapping(value = "/studentGroups/addStudent")
    public ResponseEntity<Object> addStudentsToGroup(@RequestBody AddStudentToGroup addStudentToGroup){
        Long studentGroupId = addStudentToGroup.getStudentGroupId();
        Set<Long> studentIds = addStudentToGroup.getStudentIds();
        StudentGroup studentGroup = studentGroupRepository.findStudentGroupById(studentGroupId);
        if (studentGroup == null){
            return ResponseEntity.badRequest().build();
        }
        for (Long studentId : studentIds) {
            Student student = studentRepository.findStudentById(studentId);
            if (student == null){
                return ResponseEntity.badRequest().build();
            } else if (student.getStudentGroup() != null) {
                return ResponseEntity.badRequest().build();
            }
        }
        for (Long studentId : studentIds) {
            Student student = studentRepository.findStudentById(studentId);
            student.setStudentGroup(studentGroup);
            student.setDepartment(studentGroup.getUniversitySupervisor().getDepartment());
            student.setUniversitySupervisor(studentGroup.getUniversitySupervisor());
            studentRepository.save(student);
        }
        return ResponseEntity.ok().build();
    }
}
