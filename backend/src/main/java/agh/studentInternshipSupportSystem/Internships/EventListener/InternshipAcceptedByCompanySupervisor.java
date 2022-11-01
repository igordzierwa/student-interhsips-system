package agh.studentInternshipSupportSystem.Internships.EventListener;

import agh.studentInternshipSupportSystem.Internships.Internship;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter @Setter
public class InternshipAcceptedByCompanySupervisor extends ApplicationEvent {
    private Internship internship;

    private String token;

    public InternshipAcceptedByCompanySupervisor(Object source, Internship internship) {
        super(source);
        this.internship = internship;
     //   this.token = token;
    }
}
