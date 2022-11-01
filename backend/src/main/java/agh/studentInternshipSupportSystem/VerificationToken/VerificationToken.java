package agh.studentInternshipSupportSystem.VerificationToken;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public class VerificationToken {
    public static final int EXPIRATION = 60 * 424;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Timestamp expiresAt;

    @CreationTimestamp
    public Timestamp createdAt;

    @Column(length = 50)
    public String token;


    public Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public VerificationToken(String token) {
        this.expiresAt = new Timestamp(calculateExpiryDate(EXPIRATION).getTime());
        this.token = token;
    }


}
