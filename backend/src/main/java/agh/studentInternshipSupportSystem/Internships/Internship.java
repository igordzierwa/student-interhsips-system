package agh.studentInternshipSupportSystem.Internships;

import agh.studentInternshipSupportSystem.CompanySupervisors.CompanySupervisor;
import agh.studentInternshipSupportSystem.Students.Student;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "internships")
@Getter
@Setter
@NoArgsConstructor
public class Internship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean universitySupervisorAccepted;
    private boolean companySupervisorAccepted;
    private LocalDate dateStarted;
    private LocalDate dateFinished;
    private String report;
    private Long gradesFromCompanySupervisor;
    private String additionalInformation;
    private Long gradeFromUniversitySupervisor;
    private Long hoursCount;
    private boolean editionNotAllowed;


    private Boolean isPaid;
    private String studentPosition;
    private String studentAddress;
    private String fieldOfStudy;
    private Long number_index;


    @OneToOne
    private Student student;

    @OneToOne
    private CompanySupervisor companySupervisor;


    @Override
    public String toString() {
        return "Internship{" +
                "id=" + id +
                ", universitySupervisorAccepted=" + universitySupervisorAccepted +
                ", companySupervisorAccepted=" + companySupervisorAccepted +
                ", dateStarted=" + dateStarted +
                ", dateFinished=" + dateFinished +
                ", report='" + report + '\'' +
                ", gradesFromCompanySupervisor=" + gradesFromCompanySupervisor +
                ", additionalInformation='" + additionalInformation + '\'' +
                ", gradeFromUniversitySupervisor=" + gradeFromUniversitySupervisor +
                ", hoursCount=" + hoursCount +
                ", editionNotAllowed=" + editionNotAllowed +
                ", isPaid=" + isPaid +
                ", studentPosition='" + studentPosition + '\'' +
                ", studentAddress='" + studentAddress + '\'' +
                ", fieldOfStudy='" + fieldOfStudy + '\'' +
                ", number_index=" + number_index +
                ", student=" + student +
                ", companySupervisor=" + companySupervisor +
                '}';
    }
}
