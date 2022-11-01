package agh.studentInternshipSupportSystem.Messager;

import agh.studentInternshipSupportSystem.Messager.RequestBody.AddMessage;
import agh.studentInternshipSupportSystem.User.User;
import agh.studentInternshipSupportSystem.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class MessageController {
    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasAnyAuthority('admin', 'student', 'university_supervisor', 'company_supervisor')")
    @GetMapping("/messages-received/{userID}")
    ResponseEntity<Object> getReceivedMessages(@PathVariable Long userID) {
        return ResponseEntity.ok(messagesRepository.findAllByRecipient(userRepository.findById(userID).get()));

    }

    @PreAuthorize("hasAnyAuthority('admin', 'student', 'university_supervisor', 'company_supervisor')")
    @GetMapping("/messages-sent/{userID}")
    ResponseEntity<Object> getSentMessages(@PathVariable Long userID) {
        return ResponseEntity.ok(messagesRepository.findAllBySender(userRepository.findById(userID).get()));
    }

    @PreAuthorize("hasAnyAuthority('admin', 'student', 'university_supervisor', 'company_supervisor')")
    @PostMapping("/messages-sent")
    ResponseEntity<Object> sendMessage(@RequestBody @Valid AddMessage addMessage) {
        Optional<User> sender = userRepository.findById(addMessage.getSenderID());
        if (!sender.isPresent()) {
            return ResponseEntity.badRequest().body("\"status\": \"sender does not exist\"");
        }

        Optional<User> recipient = userRepository.findById(addMessage.getRecipientID());
        if (!recipient.isPresent()) {
            return ResponseEntity.badRequest().body("\"status\": \"recipient does not exist\"");
        }

        Message newMessage = new Message(addMessage);
        newMessage.setSender(sender.get());
        newMessage.setRecipient(recipient.get());
        messagesRepository.save(newMessage);

        return ResponseEntity.ok("\"status\": \"message sent\"");
    }

    @PreAuthorize("hasAnyAuthority('admin', 'student', 'university_supervisor', 'company_supervisor')")
    @GetMapping("/messages/{id}")
    ResponseEntity<Object> getSpecificMessage(@PathVariable Long id) {
        return ResponseEntity.ok(messagesRepository.findById(id).get());
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/messages/{id}")
    ResponseEntity<Object> deleteMessage(@PathVariable Long id) {
        messagesRepository.delete(messagesRepository.findById(id).get());
        return ResponseEntity.ok("\"status\": \"message deleted\"");
    }

    @PreAuthorize("hasAnyAuthority('admin', 'student', 'university_supervisor', 'company_supervisor')")
    @DeleteMapping("/messages-sent/{id}")
    ResponseEntity<Object> deleteMessageSent(@PathVariable Long id) {
        Message message = messagesRepository.findById(id).get();
        message.setSender(null);
        messagesRepository.save(message);
        return ResponseEntity.ok("\"status\": \"message sent deleted from user\"");
    }

    @PreAuthorize("hasAnyAuthority('admin', 'student', 'university_supervisor', 'company_supervisor')")
    @DeleteMapping("/messages-received/{id}")
    ResponseEntity<Object> deleteMessageReceived(@PathVariable Long id) {
        Message message = messagesRepository.findById(id).get();
        message.setRecipient(null);
        messagesRepository.save(message);
        return ResponseEntity.ok("\"status\": \"message received deleted from user\"");
    }
}
