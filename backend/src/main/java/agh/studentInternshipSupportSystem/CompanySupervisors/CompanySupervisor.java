package agh.studentInternshipSupportSystem.CompanySupervisors;

import agh.studentInternshipSupportSystem.Companies.Company;
import agh.studentInternshipSupportSystem.CompanySupervisors.RequestBody.AddCompanySupervisor;
import agh.studentInternshipSupportSystem.User.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "company_supervisors")
@Getter @Setter
public class CompanySupervisor extends User {
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private boolean active;

    public CompanySupervisor(){
        this.active = false;
    }

    public CompanySupervisor(AddCompanySupervisor addCompanySupervisor) {
     //   this.setUsername(addCompanySupervisor.getUsername());
        this.setName(addCompanySupervisor.getName());
        this.setSurname(addCompanySupervisor.getSurname());
        this.setEmail(addCompanySupervisor.getEmail());
    }
}
