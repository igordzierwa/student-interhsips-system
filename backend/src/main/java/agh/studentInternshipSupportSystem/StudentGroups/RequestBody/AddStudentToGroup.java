package agh.studentInternshipSupportSystem.StudentGroups.RequestBody;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter @Setter
public class AddStudentToGroup {
    @NotNull(message = "Student Group ID is mandatory")
    private Long studentGroupId;

    @NotNull(message = "Student IDs are mandatory")
    private Set<Long> studentIds;
}
