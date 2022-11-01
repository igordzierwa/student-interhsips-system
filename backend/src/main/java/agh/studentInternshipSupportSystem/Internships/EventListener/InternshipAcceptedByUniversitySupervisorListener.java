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
public class InternshipAcceptedByUniversitySupervisorListener implements ApplicationListener<InternshipAcceptedByUniversitySupervisor> {
    @Autowired
    private MailSender mailSender;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private EmailTemplates emailTemplates;

    @Autowired
    InternshipRepository  internshipRepository;

    @Override
    public void onApplicationEvent(InternshipAcceptedByUniversitySupervisor onInternshipAcceptedByUniversitySupervisor) {

        Email email = emailTemplates.getEmailTemplate("internshipAcceptedByUniversitySupervisor");

        executorService.execute(() -> {
            mailSender.sendEmail(onInternshipAcceptedByUniversitySupervisor.getInternship().getStudent().getEmail(),
                    email.getSubject(), email.getMessage());
            log.info("SENT!");
        });
    }
}
