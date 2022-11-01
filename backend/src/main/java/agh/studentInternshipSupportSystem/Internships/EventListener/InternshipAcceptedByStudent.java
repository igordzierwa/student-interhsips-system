package agh.studentInternshipSupportSystem.Internships.EventListener;

import agh.studentInternshipSupportSystem.Internships.Internship;
import agh.studentInternshipSupportSystem.UniversitySupervisors.UniversitySupervisor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter @Setter
public class InternshipAcceptedByStudent extends ApplicationEvent {
    private Internship internship;

    private String token;

    public InternshipAcceptedByStudent(Object source, Internship internship) {
        super(source);
        this.internship = internship;
     //   this.token = token;
    }
}
