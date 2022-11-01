package agh.studentInternshipSupportSystem.VerificationToken;

import agh.studentInternshipSupportSystem.CompanySupervisors.CompanySupervisor;
import agh.studentInternshipSupportSystem.Internships.Internship;
import agh.studentInternshipSupportSystem.User.User;
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
@Table(name = "verification_token_cs")
@Getter @Setter @NoArgsConstructor
public class VerificationTokenInternshipCS extends VerificationToken {


    @OneToOne()
    @JoinColumn(name = "internship_id")
    private Internship internship;

    @OneToOne()
    @JoinColumn(name = "cs_id")
    private CompanySupervisor companySupervisor;

    public VerificationTokenInternshipCS(Internship internship, CompanySupervisor companySupervisor, String token) {
        super(token);
        this.internship = internship;
        this.companySupervisor = companySupervisor;
        this.expiresAt = new Timestamp(calculateExpiryDate(EXPIRATION).getTime());
        this.token = token;
    }

}