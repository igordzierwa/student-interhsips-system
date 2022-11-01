package agh.studentInternshipSupportSystem.Mail.Templates;

import org.springframework.stereotype.Service;

@Service
public class EmailTemplatesImpl implements EmailTemplates {
    @Override
    public Email getEmailTemplate(String messageType) {
        switch (messageType){
            case "universitySupervisorAccountCreation":
                return new Email(
                        "Założono nowe konto w systemie SWOPS",
                        "Nowe konto uczelnianego opiekuna praktyk." +
                                "Aby uzyskać dostęp - kliknij w link i załóż hasło.\n" +
                                "https://swops.pl/university-supervisors/activate?token="
                    );
            case "companySupervisorAccountCreation":
                return new Email(
                        "Założono nowe konto w systemie SWOPS",
                        "Nowe konto firmowego opiekuna praktyk." +
                                "Aby uzyskać dostęp - kliknij w link i załóż hasło.\n" +
                                "https://swops.pl/company-supervisors/activate?token="
                );
            case "internshipAcceptedByStudent":
                return new Email(
                        "Masz nową praktykę do akceptacji w SWOPS",
                        "Masz nową praktykę do akceptacji." +
                                "Przejdź do systemu, sprawdź dane i zaakceptuj: \n" +
                                "https://swops.pl/company-supervisors/accept-internship?token="
                );
            case "internshipAcceptedByCompanySupervisor":
                return new Email(
                        "Masz nową praktykę do akceptacji w SWOPS",
                        "Masz nową praktykę do akceptacji." +
                                "Przejdź do systemu, sprawdź dane i zaakceptuj: \n" +
                                "https://swops.pl/university-supervisors/accept-internship?token="
                );
            case "internshipAcceptedByUniversitySupervisor":
                return new Email(
                        "Gratulacje! Twoja praktyka została zaliczona",
                        "Twoja praktyka została zaliczona." +
                                "W systemie SWOPS masz możliwość wygenerowania certyfikatu w PDF"
                );
        }
        return null;
    }
}
