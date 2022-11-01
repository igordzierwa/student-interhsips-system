package agh.studentInternshipSupportSystem.Students.RequestBody;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class RegisterStudent {
    @NotNull(message = "Username is madatory")
    private String username;

    @NotNull(message = "Password is mandatory")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    @NotNull(message = "Confirm password is mandatory")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String confirmPassword;

    @NotNull(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Surname is mandatory")
    private String surname;

    @NotNull(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;
}
