package agh.studentInternshipSupportSystem.Stats;

import agh.studentInternshipSupportSystem.Companies.CompanyRepository;
import agh.studentInternshipSupportSystem.CompanySupervisors.CompanySupervisorRepository;
import agh.studentInternshipSupportSystem.Departments.DepartmentRepository;
import agh.studentInternshipSupportSystem.Roles.Role;
import agh.studentInternshipSupportSystem.Roles.RoleRepository;
import agh.studentInternshipSupportSystem.Roles.RolesName;
import agh.studentInternshipSupportSystem.Stats.RequestBody.DefaultStats;
import agh.studentInternshipSupportSystem.StudentGroups.StudentGroup;
import agh.studentInternshipSupportSystem.Students.RequestBody.RegisterStudent;
import agh.studentInternshipSupportSystem.Students.Student;
import agh.studentInternshipSupportSystem.Students.StudentRepository;
import agh.studentInternshipSupportSystem.UniversitySupervisors.UniversitySupervisorRepository;
import agh.studentInternshipSupportSystem.User.User;
import agh.studentInternshipSupportSystem.User.UserRepository;
import agh.studentInternshipSupportSystem.User.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class StatsController {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CompanySupervisorRepository companySupervisorRepository;

    @Autowired
    UniversitySupervisorRepository universitySupervisorRepository;

    @Autowired
    CompanyRepository  companyRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    private RoleRepository roleRepository;


    @GetMapping(value = "/defaultStats")
    public ResponseEntity<Object> defaultStats() {
        DefaultStats data = new DefaultStats();

        Role admin  = roleRepository.findByName("admin");
        List<User> adminUser = userRepository.findUsersByRoles(admin);

        data.setStudentCount(studentRepository.count());
        data.setCsCount(companySupervisorRepository.count());
        data.setUsCount(universitySupervisorRepository.count());
        data.setAdminCount((long) adminUser.size());

        data.setCompanyCount(companyRepository.count());
        data.setDepartmentCount(departmentRepository.count());


        return ResponseEntity.ok(data);
    }

    @GetMapping(value = "/stats/{role}/{year}")
    public ResponseEntity<Object> studentsByYear(@PathVariable String role, @PathVariable int year) throws ParseException {
        Role r = roleRepository.findByName(role);

        List<Integer> count = new LinkedList<>();
        for (int i = 1; i <= 12; i++) {
            count.add(userRepository.countAllByCreatedDateAfterAndCreatedDateBeforeAndRoles(
                    new SimpleDateFormat("yyyy-MM-dd").parse(year + "-"+i+"-01"),
                    new SimpleDateFormat("yyyy-MM-dd").parse(year + "-"+i+"-31"),
                    r
            ));
        }

        return ResponseEntity.ok(count);
    }

    @GetMapping(value = "/stats-departments/")
    public ResponseEntity<Object> departmentsPopular(){
        return ResponseEntity.ok(studentRepository.getPopularDepartments());
    }
}

