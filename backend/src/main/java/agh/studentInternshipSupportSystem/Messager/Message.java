package agh.studentInternshipSupportSystem.Messager;

import agh.studentInternshipSupportSystem.Messager.RequestBody.AddMessage;
import agh.studentInternshipSupportSystem.User.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Data
@Table(name = "messeges")
@Getter @Setter @NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;

    private String message;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User recipient;

    public Message(AddMessage addMessage) {
        this.topic = addMessage.getTopic();
        this.message = addMessage.getMessage();
    }
}
