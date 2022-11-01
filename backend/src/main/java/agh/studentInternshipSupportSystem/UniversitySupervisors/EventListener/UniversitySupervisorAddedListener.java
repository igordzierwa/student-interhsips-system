package agh.studentInternshipSupportSystem.UniversitySupervisors.EventListener;

import agh.studentInternshipSupportSystem.Mail.Templates.Email;
import agh.studentInternshipSupportSystem.Mail.Templates.EmailTemplates;
import agh.studentInternshipSupportSystem.Mail.MailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
public class UniversitySupervisorAddedListener implements ApplicationListener<UniversitySupervisorAddedEvent> {
    @Autowired
    private MailSender mailSender;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private EmailTemplates emailTemplates;

    @Override
    public void onApplicationEvent(UniversitySupervisorAddedEvent onUniversitySupervisorAdded) {
        log.info("On App Event Start");
        Email email = emailTemplates.getEmailTemplate("universitySupervisorAccountCreation");

        executorService.execute(() -> {
            mailSender.sendEmail(onUniversitySupervisorAdded.getUniversitySupervisor().getEmail(),
                    email.getSubject(), email.getMessage() + onUniversitySupervisorAdded.getToken());
            log.info("SENT!");
        });
    }
}
