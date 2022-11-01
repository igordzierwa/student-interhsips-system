package agh.studentInternshipSupportSystem.Messager;

import agh.studentInternshipSupportSystem.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByRecipient(User user);
    List<Message> findAllBySender(User user);
}
