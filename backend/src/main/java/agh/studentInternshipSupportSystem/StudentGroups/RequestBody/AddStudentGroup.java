package agh.studentInternshipSupportSystem.StudentGroups.RequestBody;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class AddStudentGroup {
    @NotNull(message = "University Supervisor Id is mandatory")
    private Long universitySupervisorId;

    @NotNull(message = "Student Group is mandatory")
    private String studentGroupName;
}