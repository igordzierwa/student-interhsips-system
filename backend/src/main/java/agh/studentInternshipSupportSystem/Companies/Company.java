package agh.studentInternshipSupportSystem.Companies;

import agh.studentInternshipSupportSystem.Companies.RequestBody.AddCompany;
import agh.studentInternshipSupportSystem.Companies.RequestBody.Address;
import agh.studentInternshipSupportSystem.CompanySupervisors.CompanySupervisor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "companies")
@Getter @Setter @NoArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;

    private String nip;

    private String regon;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "company")
    @JsonIgnore
    Set<CompanySupervisor> companySupervisors;

    public Company(AddCompany addCompany) {
        this.companyName = addCompany.getCompanyName();
        this.nip = addCompany.getNip();
        this.regon = addCompany.getRegon();
        this.address = addCompany.getAddress();
        this.companySupervisors = new HashSet<>();
    }
}
