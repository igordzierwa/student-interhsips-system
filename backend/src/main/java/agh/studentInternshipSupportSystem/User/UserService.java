package agh.studentInternshipSupportSystem.User;

import agh.studentInternshipSupportSystem.CompanySupervisors.RequestBody.AddCompanySupervisor;
import agh.studentInternshipSupportSystem.Students.RequestBody.RegisterStudent;
import agh.studentInternshipSupportSystem.UniversitySupervisors.RequestBody.AddUniversitySupervisor;

public interface UserService {
    void addAdminAccIfNotExist();
    void save(User user, String roleName);
    void editStudent(RegisterStudent student, Long id);
    void editCompanySupervisor(AddCompanySupervisor addCompanySupervisor, Long id);
    void editUniversitySupervisor(AddUniversitySupervisor addUniversitySupervisor, Long id);
}


