package agh.studentInternshipSupportSystem.VerificationToken;
import agh.studentInternshipSupportSystem.User.User;



public interface VerificationTokenUserRepository extends VerificationTokenRepository<VerificationTokenUser, Long>{
    VerificationTokenUser findVerificationTokenByToken(String token);
    VerificationTokenUser findVerificationTokenByUser(User user);
}

