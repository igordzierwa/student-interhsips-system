package agh.studentInternshipSupportSystem.Stats.RequestBody;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DefaultStats {
    private Long studentCount;
    private Long usCount;
    private Long csCount;
    private Long adminCount;
    private Long companyCount;
    private  Long departmentCount;

}
