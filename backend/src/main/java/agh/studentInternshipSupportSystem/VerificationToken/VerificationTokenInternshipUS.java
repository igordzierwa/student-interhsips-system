package agh.studentInternshipSupportSystem.VerificationToken;

import agh.studentInternshipSupportSystem.CompanySupervisors.CompanySupervisor;
import agh.studentInternshipSupportSystem.Internships.Internship;
import agh.studentInternshipSupportSystem.UniversitySupervisors.UniversitySupervisor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "verification_token_us")
@Getter @Setter @NoArgsConstructor
public class VerificationTokenInternshipUS extends VerificationToken {


    @OneToOne()
    @JoinColumn(name = "internship_id")
    private Internship internship;

    @OneToOne()
    @JoinColumn(name = "us_id")
    private UniversitySupervisor universitySupervisor;

    public VerificationTokenInternshipUS(Internship internship, UniversitySupervisor universitySupervisor, String token) {
        super(token);
        this.internship = internship;
        this.universitySupervisor = universitySupervisor;
        this.expiresAt = new Timestamp(calculateExpiryDate(EXPIRATION).getTime());
        this.token = token;
    }

}