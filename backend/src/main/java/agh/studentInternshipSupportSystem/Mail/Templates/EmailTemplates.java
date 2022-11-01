package agh.studentInternshipSupportSystem.Mail.Templates;

import org.springframework.stereotype.Service;

@Service
public interface EmailTemplates {
    Email getEmailTemplate(String messageType);
}
