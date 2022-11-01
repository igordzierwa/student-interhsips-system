package agh.studentInternshipSupportSystem.User;

import agh.studentInternshipSupportSystem.Roles.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findUsersByRoles(Role role);
    int countAllByCreatedDateAfterAndCreatedDateBeforeAndRoles(Date after, Date before, Role role);

}
