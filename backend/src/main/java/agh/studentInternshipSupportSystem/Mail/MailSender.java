package agh.studentInternshipSupportSystem.Mail;

public interface MailSender {
    void sendEmail(String to, String subject, String text);
}
