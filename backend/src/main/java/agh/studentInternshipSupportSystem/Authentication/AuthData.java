package agh.studentInternshipSupportSystem.Authentication;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class AuthData {
    @NotNull(message = "Username is mandatory")
    private String username;

    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;
}