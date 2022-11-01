package agh.studentInternshipSupportSystem.Internships.RequestBody;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class EditStudentFields {
    @NotNull
    private LocalDate dateStarted;
    @NotNull
    private LocalDate dateFinished;
    @NotNull
    private Long hoursCount;
    @NotNull
    private String report;
}
