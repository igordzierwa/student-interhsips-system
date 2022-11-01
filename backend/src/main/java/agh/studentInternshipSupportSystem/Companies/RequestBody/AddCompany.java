package agh.studentInternshipSupportSystem.Companies.RequestBody;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.pl.NIP;
import org.hibernate.validator.constraints.pl.REGON;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class AddCompany {
    @NotNull(message = "Company name is mandatory")
    private String companyName;

    @NotNull(message = "NIP is mandatory")
    @NIP(message = "NIP should be valid and existing")
    private String nip;

    @NotNull(message = "REGON is mandatory")
    @REGON(message = "REGON shoud be valid and existing")
    private String regon;

    @NotNull(message = "Address is mandatory")
    private Address address;
}
