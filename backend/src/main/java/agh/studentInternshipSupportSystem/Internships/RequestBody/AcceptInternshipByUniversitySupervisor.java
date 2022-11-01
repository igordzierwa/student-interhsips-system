package agh.studentInternshipSupportSystem.Internships.RequestBody;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AcceptInternshipByUniversitySupervisor {
    @NotNull
    private String token;
    @NotNull
    private Long grade;
}
