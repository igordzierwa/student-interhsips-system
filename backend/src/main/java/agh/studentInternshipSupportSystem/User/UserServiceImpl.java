package agh.studentInternshipSupportSystem.User;

import agh.studentInternshipSupportSystem.Companies.CompanyRepository;
import agh.studentInternshipSupportSystem.CompanySupervisors.CompanySupervisor;
import agh.studentInternshipSupportSystem.CompanySupervisors.RequestBody.AddCompanySupervisor;
import agh.studentInternshipSupportSystem.Departments.DepartmentRepository;
import agh.studentInternshipSupportSystem.Roles.Role;
import agh.studentInternshipSupportSystem.Roles.RoleRepository;
import agh.studentInternshipSupportSystem.Roles.RolesName;
import agh.studentInternshipSupportSystem.Students.RequestBody.RegisterStudent;
import agh.studentInternshipSupportSystem.Students.Student;
import agh.studentInternshipSupportSystem.UniversitySupervisors.RequestBody.AddUniversitySupervisor;
import agh.studentInternshipSupportSystem.UniversitySupervisors.UniversitySupervisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void addAdminAccIfNotExist(){
        if(userRepository.findByUsername("admin") == null){
            User admin = new User();
            HashSet<Role> hs = new HashSet<>();
            hs.add(roleRepository.findByName(RolesName.ADMIN.getRoleName()));
            admin.setRoles(hs);
            admin.setName("ADMIN");
            admin.setSurname("ADMIN");
            admin.setPassword("admin");
            admin.setUsername("admin");
            admin.setPassword(bCryptPasswordEncoder.encode(admin.getPassword()));
            userRepository.save(admin);
            log.info("Admin added");
        } else {
            log.info("Admin exists");
        }
    }

    @Override
    public void save(User user, String roleName) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        HashSet<Role> hs = new HashSet<>();
        hs.add(roleRepository.findByName(roleName));
        user.setRoles(hs);
        userRepository.save(user);
    }

    @Override
    public void editStudent(RegisterStudent student, Long id) {
        Student existStudent = (Student) userRepository.findById(id).get();

        existStudent.setUsername(student.getUsername());
        existStudent.setName(student.getName());
        existStudent.setSurname(student.getSurname());
        existStudent.setPassword(student.getPassword());
        existStudent.setEmail(student.getEmail());

        userRepository.save(existStudent);
    }

    @Override
    public void editCompanySupervisor(AddCompanySupervisor addCompanySupervisor, Long id) {
        CompanySupervisor existCompanySupervisor = (CompanySupervisor) userRepository.findById(id).get();

        //existCompanySupervisor.setUsername(addCompanySupervisor.getUsername());
        existCompanySupervisor.setName(addCompanySupervisor.getName());
        existCompanySupervisor.setSurname(addCompanySupervisor.getSurname());
        existCompanySupervisor.setEmail(addCompanySupervisor.getEmail());
        existCompanySupervisor.setCompany(companyRepository
                .findById(addCompanySupervisor.getCompanyId()).get());

        userRepository.save(existCompanySupervisor);
    }

    @Override
    public void editUniversitySupervisor(AddUniversitySupervisor addUniversitySupervisor, Long id) {
        UniversitySupervisor existUniversitySupervisor = (UniversitySupervisor) userRepository.findById(id).get();

        existUniversitySupervisor.setUsername(addUniversitySupervisor.getUsername());
        existUniversitySupervisor.setName(addUniversitySupervisor.getName());
        existUniversitySupervisor.setSurname(addUniversitySupervisor.getSurname());
        existUniversitySupervisor.setEmail(addUniversitySupervisor.getEmail());
        existUniversitySupervisor.setDepartment(departmentRepository
                .findById(addUniversitySupervisor.getDepartmentId()).get());

        userRepository.save(existUniversitySupervisor);
    }
}

