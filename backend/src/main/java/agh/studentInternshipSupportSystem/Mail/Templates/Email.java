package agh.studentInternshipSupportSystem.Mail.Templates;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Email {
    private final String subject;
    private final String message;

    public Email(String subject, String message) {
        this.subject = subject;
        this.message = message;
    }
}
