package agh.studentInternshipSupportSystem.Roles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleRepository roleRepository;

    public void makeRoles(){
        Role admin = new Role();
        admin.setName(RolesName.ADMIN.getRoleName());

        Role student = new Role();
        student.setName(RolesName.STUDENT.getRoleName());

        Role companySupervisor = new Role();
        companySupervisor.setName(RolesName.COMPANY_SUPERVISOR.getRoleName());

        Role universitySupervisor = new Role();
        universitySupervisor.setName(RolesName.UNIVERSITY_SUPERVISOR.getRoleName());

        roleRepository.save(admin);
        roleRepository.save(student);
        roleRepository.save(companySupervisor);
        roleRepository.save(universitySupervisor);
    }
    public boolean checkRoles(){
        Role admin = roleRepository.findByName(RolesName.ADMIN.getRoleName());
        Role student = roleRepository.findByName(RolesName.STUDENT.getRoleName());
        Role companySupervisor = roleRepository.findByName(RolesName.COMPANY_SUPERVISOR.getRoleName());
        Role universitySupervisor = roleRepository.findByName(RolesName.UNIVERSITY_SUPERVISOR.getRoleName());

        if(admin != null & student != null & companySupervisor != null & universitySupervisor != null){
            return false;
        } else {
            return true;
        }

    }
}
