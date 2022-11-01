package agh.studentInternshipSupportSystem.UniversitySupervisors.RequestBody;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ActivateUniversitySupervisor {
    @NotNull(message = "Username is mandatory")
    private String username;

    @NotNull(message = "Password is mandatory")
    private String password;

    @NotNull(message = "Confirm password is mandatory")
    private String confirmPassword;

    @NotNull(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;
}
