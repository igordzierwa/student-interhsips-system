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
public class InternshipAcceptedByCompanySupervisorListener implements ApplicationListener<InternshipAcceptedByCompanySupervisor> {
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
    public void onApplicationEvent(InternshipAcceptedByCompanySupervisor onInternshipAcceptedByCompanySupervisor) {
        String token = tokenGenerator.generateAuthToken(
                onInternshipAcceptedByCompanySupervisor.getInternship(),
                onInternshipAcceptedByCompanySupervisor.getInternship().getStudent().getUniversitySupervisor());

        Email email = emailTemplates.getEmailTemplate("internshipAcceptedByCompanySupervisor");

        executorService.execute(() -> {
            mailSender.sendEmail(onInternshipAcceptedByCompanySupervisor.getInternship().getStudent().getUniversitySupervisor().getEmail(),
                    email.getSubject(), email.getMessage() + token);
            log.info("SENT!");
        });
    }
}
