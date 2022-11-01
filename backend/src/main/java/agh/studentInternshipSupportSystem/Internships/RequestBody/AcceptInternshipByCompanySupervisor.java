package agh.studentInternshipSupportSystem.Internships.RequestBody;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class AcceptInternshipByCompanySupervisor {
    @NotNull
    private String token;
    @NotNull
    private Long grade;
    @NotNull
    private String review;
}
