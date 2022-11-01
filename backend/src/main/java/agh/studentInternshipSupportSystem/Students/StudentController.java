package agh.studentInternshipSupportSystem.Students;

import agh.studentInternshipSupportSystem.Roles.RoleRepository;
import agh.studentInternshipSupportSystem.Roles.RolesName;
import agh.studentInternshipSupportSystem.StudentGroups.StudentGroup;
import agh.studentInternshipSupportSystem.Students.RequestBody.RegisterStudent;
import agh.studentInternshipSupportSystem.User.User;
import agh.studentInternshipSupportSystem.User.UserRepository;
import agh.studentInternshipSupportSystem.User.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class StudentController {
    @Autowired
    private UserService userService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping(value = "/registration")
    public ResponseEntity<Object> registerStudent(@RequestBody @Valid RegisterStudent registerStudent) {
        if (registerStudent.getPassword().equals(registerStudent.getConfirmPassword())) {
            Student student = new Student(registerStudent);
            userService.save(student, RolesName.STUDENT.getRoleName());
            return ResponseEntity.ok("\"status\": \"student registered\"");
        } else {
            return ResponseEntity.badRequest().body("\"status\": \"passwords are different\"");
        }
    }

    @GetMapping(value = "/students")
    public ResponseEntity<Object> getAllStudents() {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    @GetMapping(value = "/students/{id}")
    public ResponseEntity<Object> getSpecificStudent(@PathVariable Long id) {
        return ResponseEntity.ok(userRepository.findById(id).get());
    }

    @GetMapping(value = "/check/email/{email}")
    public ResponseEntity<Object> checkEmailAvailability(@PathVariable String email){
        User user = userRepository.findByEmail(email);
        if (user == null){
            return ResponseEntity.ok("\"true\"");
        }
        return ResponseEntity.ok("\"false\"");
    }

    @GetMapping(value = "/check/username/{username}")
    public ResponseEntity<Object> checkUsernameAvailability(@PathVariable String username){
        User user = userRepository.findByUsername(username);
        if (user == null){
            return ResponseEntity.ok("\"true\"");
        }
        return ResponseEntity.ok("\"false\"");
    }

    @GetMapping(value = "/students/groups")
    @PreAuthorize("hasAuthority('student')")
    public ResponseEntity<Object> getStudentGroup(@RequestParam String username) {
        User user = userRepository.findByUsername(username);
        StudentGroup studentGroup;
        if (user == null) {
            return ResponseEntity.noContent().build();
        }
        if (user.getRoles().contains(roleRepository.findByName(RolesName.STUDENT.getRoleName()))) {
            Student student = studentRepository.findStudentByUsername(user.getUsername());
            studentGroup = student.getStudentGroup();
        } else {
            return ResponseEntity.noContent().build();
        }
        if (studentGroup == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(studentGroup);
    }

    @PostMapping(value = "/students/{id}")
    public ResponseEntity<Object> editStudent(@RequestBody @Valid RegisterStudent registerStudent,
                                              @PathVariable Long id) {
        if (registerStudent.getPassword().equals(registerStudent.getConfirmPassword())) {
            userService.editStudent(registerStudent, id);
            return ResponseEntity.ok("\"status\": \"student edited\"");
        } else {
            return ResponseEntity.badRequest().body("\"status\": \"passwords are different\"");
        }
    }

    @DeleteMapping(value = "/students/{id}")
    public ResponseEntity<Object> deleteSpecificStudent(@PathVariable Long id) {
        studentRepository.delete((Student)userRepository.findById(id).get());
        return ResponseEntity.ok("\"status\": \"student deleted\"");
    }

    @GetMapping(value="/studentsWithoutGroupAssigned")
    @PreAuthorize("hasAnyAuthority('admin', 'university_supervisor')")
    public ResponseEntity<Object> getStudentsWithoutGroupAssigned(){
        List<Student> allStudents = studentRepository.findAll();
        List<Student> studentsWithoutGroupAssigned = new ArrayList<>();
        for (Student student : allStudents) {
            if (student.getStudentGroup() == null){
                studentsWithoutGroupAssigned.add(student);
            }
        }
        return ResponseEntity.ok(studentsWithoutGroupAssigned);
    }
}
