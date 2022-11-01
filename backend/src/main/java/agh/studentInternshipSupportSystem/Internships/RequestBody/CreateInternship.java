package agh.studentInternshipSupportSystem.Internships.RequestBody;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateInternship {

    @NotNull
    private Long companyId;

    @NotNull
    private Long companySupervisorId;

    @NotNull
    private Boolean payment;

    @NotNull
    private String position;

    @NotNull
    private String address;

    @NotNull
    private String fieldOfStudy;

    @NotNull
    private Long number_index;



}
