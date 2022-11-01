package agh.studentInternshipSupportSystem.CompanySupervisors.EventListener;

import agh.studentInternshipSupportSystem.Mail.MailSender;
import agh.studentInternshipSupportSystem.Mail.Templates.Email;
import agh.studentInternshipSupportSystem.Mail.Templates.EmailTemplates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import java.util.concurrent.ExecutorService;

@Component
@Slf4j
public class CompanySupervisorAddedListener implements ApplicationListener<CompanySupervisorAddedEvent> {
    @Autowired
    private MailSender mailSender;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private EmailTemplates emailTemplates;

    @Override
    public void onApplicationEvent(CompanySupervisorAddedEvent onCompanySupervisorAddedEvent) {
        log.info("On App Event Start");
        Email email = emailTemplates.getEmailTemplate("companySupervisorAccountCreation");

        executorService.execute(() -> {
            mailSender.sendEmail(onCompanySupervisorAddedEvent.getCompanySupervisor().getEmail(),
                    email.getSubject(), email.getMessage() + onCompanySupervisorAddedEvent.getToken());
            log.info("SENT!");
        });
    }
}
