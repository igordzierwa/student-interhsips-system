package agh.studentInternshipSupportSystem.Roles;

import agh.studentInternshipSupportSystem.User.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter @Setter @NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles", cascade = CascadeType.REMOVE)
    private Set<User> users;
}
