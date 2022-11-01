package agh.studentInternshipSupportSystem.Internships.EventListener;

import agh.studentInternshipSupportSystem.Internships.InternshipRepository;
import agh.studentInternshipSupportSystem.Mail.MailSender;
import agh.studentInternshipSupportSystem.Mail.Templates.Email;
import agh.studentInternshipSupportSystem.Mail.Templates.EmailTemplates;
import agh.studentInternshipSupportSystem.VerificationToken.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Slf4j
@Component
public class InternshipAcceptedByStudentListener implements ApplicationListener<InternshipAcceptedByStudent> {
    @Autowired
    private MailSender mailSender;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private EmailTemplates emailTemplates;

    @Autowired
    TokenGenerator tokenGenerator;

    @Autowired
    InternshipRepository  internshipRepository;

    @Override
    public void onApplicationEvent(InternshipAcceptedByStudent onInternshipAcceptedByStudent) {
        String token = tokenGenerator.generateAuthToken(
                onInternshipAcceptedByStudent.getInternship(),
                onInternshipAcceptedByStudent.getInternship().getCompanySupervisor());

        Email email = emailTemplates.getEmailTemplate("internshipAcceptedByStudent");

        executorService.execute(() -> {
            mailSender.sendEmail(onInternshipAcceptedByStudent.getInternship().getCompanySupervisor().getEmail(),
                    email.getSubject(), email.getMessage() + token);
            log.info("SENT!");
        });
    }
}
