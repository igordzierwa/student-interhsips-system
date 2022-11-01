package agh.studentInternshipSupportSystem.VerificationToken;

import agh.studentInternshipSupportSystem.User.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

@Data
@Entity
@Table(name = "verification_token")
@Getter @Setter @NoArgsConstructor
public class VerificationTokenUser extends VerificationToken {

    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public VerificationTokenUser(User user, String token) {
        super(token);
        this.user = user;
        this.expiresAt = new Timestamp(calculateExpiryDate(EXPIRATION).getTime());
        this.token = token;
    }

}