package agh.studentInternshipSupportSystem.VerificationToken;

import agh.studentInternshipSupportSystem.CompanySupervisors.CompanySupervisor;
import agh.studentInternshipSupportSystem.Internships.Internship;
import agh.studentInternshipSupportSystem.UniversitySupervisors.UniversitySupervisor;
import agh.studentInternshipSupportSystem.User.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;

@Slf4j
@Service
public class TokenGeneratorImpl implements TokenGenerator {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    VerificationTokenInternshipUSRepository verificationTokenInternshipUSRepository;

    @Autowired
    VerificationTokenInternshipCSRepository verificationTokenInternshipCSRepository;

    public String generateAuthToken(User user){
        String token = generateTokenString();

        VerificationTokenUser verificationToken = new VerificationTokenUser(user, token);
        verificationTokenRepository.save(verificationToken);

        return token;
    }

    @Override
    public String generateAuthToken(Internship internship, UniversitySupervisor universitySupervisor) {

        String token;
        VerificationTokenInternshipUS existingToken = verificationTokenInternshipUSRepository.findVerificationTokenByInternshipAndUniversitySupervisor(internship, universitySupervisor);

        if(existingToken == null) {
            token = generateTokenString();
            VerificationTokenInternshipUS verificationTokenInternshipUS = new VerificationTokenInternshipUS(internship, universitySupervisor, token);
            verificationTokenRepository.save(verificationTokenInternshipUS);
        } else {
            token = existingToken.getToken();
        }


        return token;
    }

    @Override
    public String generateAuthToken(Internship internship, CompanySupervisor companySupervisor) {
        String token;

        VerificationTokenInternshipCS existingToken = verificationTokenInternshipCSRepository.findVerificationTokenByInternshipAndCompanySupervisor(internship, companySupervisor);

        if(existingToken == null) {
            token = generateTokenString();
            VerificationTokenInternshipCS verificationTokenInternshipCS = new VerificationTokenInternshipCS(internship, companySupervisor, token);
            verificationTokenRepository.save(verificationTokenInternshipCS);
        } else {
            token = existingToken.getToken();
        }


        return token;
    }


    private String generateTokenString() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] values = new byte[124];
        secureRandom.nextBytes(values);


        String token = this.generateSafeToken("SECRET_KEY");
        log.info(token);
        return token;
    }

    private String generateSafeToken(String salt) {
        SecureRandom random = new SecureRandom();

        String candidates = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789" + salt;
        char[] chars = candidates.toCharArray();

        StringBuilder sb = new StringBuilder(30);

        for (int i = 0; i < 30; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }

        return sb.toString();
    }
}
