package agh.studentInternshipSupportSystem.User;

import agh.studentInternshipSupportSystem.Messager.Message;
import agh.studentInternshipSupportSystem.Roles.Role;
import agh.studentInternshipSupportSystem.VerificationToken.VerificationToken;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"})
})
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter @NoArgsConstructor
public class User extends VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String name;

    private String surname;

    private String email;

    @ManyToMany
    @JsonIgnore
    private Set<Role> roles;

    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    List<Message> messagesSent;

    @OneToMany(mappedBy = "recipient")
    @JsonIgnore
    List<Message> messagesReceived;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
}

