package agh.studentInternshipSupportSystem.UniversitySupervisors;

import agh.studentInternshipSupportSystem.Departments.Department;
import agh.studentInternshipSupportSystem.StudentGroups.StudentGroup;
import agh.studentInternshipSupportSystem.UniversitySupervisors.RequestBody.AddUniversitySupervisor;
import agh.studentInternshipSupportSystem.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Data
@Table(name = "university_supervisors")
@Getter @Setter
public class UniversitySupervisor extends User {

    @OneToOne
    private Department department;

    @OneToMany(mappedBy = "universitySupervisor")
    @JsonIgnore
    private Set<StudentGroup> studentGroups;

    private boolean active;

    public UniversitySupervisor(){
        this.active = false;
    }

    public UniversitySupervisor(AddUniversitySupervisor addUniversitySupervisor) {
        this.setUsername(addUniversitySupervisor.getUsername());
        this.setName(addUniversitySupervisor.getName());
        this.setSurname(addUniversitySupervisor.getSurname());
        this.setEmail(addUniversitySupervisor.getEmail());
    }
}
