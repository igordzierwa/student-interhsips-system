package agh.studentInternshipSupportSystem.CompanySupervisors.RequestBody;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class AddCompanySupervisor {
    private String username;

    @NotNull(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Surname is mandatory")
    private String surname;

    @NotNull(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Company Id is mandatory")
    private Long companyId;
}
