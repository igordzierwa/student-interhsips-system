package agh.studentInternshipSupportSystem.UniversitySupervisors.EventListener;

import agh.studentInternshipSupportSystem.UniversitySupervisors.UniversitySupervisor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter @Setter
public class UniversitySupervisorAddedEvent extends ApplicationEvent {
    private UniversitySupervisor universitySupervisor;

    private String token;

    public UniversitySupervisorAddedEvent(Object source, UniversitySupervisor universitySupervisor, String token) {
        super(source);
        this.universitySupervisor = universitySupervisor;
        this.token = token;
    }
}
