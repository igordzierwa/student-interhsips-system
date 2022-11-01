package agh.studentInternshipSupportSystem.CompanySupervisors.EventListener;

import agh.studentInternshipSupportSystem.CompanySupervisors.CompanySupervisor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter @Setter
public class CompanySupervisorAddedEvent extends ApplicationEvent {
    private CompanySupervisor companySupervisor;

    private String token;

    public CompanySupervisorAddedEvent(Object source, CompanySupervisor companySupervisor, String token) {
        super(source);
        this.companySupervisor = companySupervisor;
        this.token = token;
    }
}
