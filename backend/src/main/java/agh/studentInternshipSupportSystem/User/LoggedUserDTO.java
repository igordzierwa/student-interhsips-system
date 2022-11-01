package agh.studentInternshipSupportSystem.User;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter @Setter
public class LoggedUserDTO {
    private Set<String> roles;
    private Long id;
    private String username;

    private String name;

    private String surname;

    private String email;

}
