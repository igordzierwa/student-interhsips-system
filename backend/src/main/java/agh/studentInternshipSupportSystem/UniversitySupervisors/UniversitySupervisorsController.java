package agh.studentInternshipSupportSystem.UniversitySupervisors;

import agh.studentInternshipSupportSystem.Departments.DepartmentRepository;
import agh.studentInternshipSupportSystem.Roles.RolesName;
import agh.studentInternshipSupportSystem.UniversitySupervisors.EventListener.UniversitySupervisorAddedEvent;
import agh.studentInternshipSupportSystem.UniversitySupervisors.RequestBody.ActivateUniversitySupervisor;
import agh.studentInternshipSupportSystem.UniversitySupervisors.RequestBody.AddUniversitySupervisor;
import agh.studentInternshipSupportSystem.User.UserRepository;
import agh.studentInternshipSupportSystem.User.UserService;
import agh.studentInternshipSupportSystem.VerificationToken.TokenGenerator;
import agh.studentInternshipSupportSystem.VerificationToken.VerificationTokenUser;
import agh.studentInternshipSupportSystem.VerificationToken.VerificationTokenRepository;
import agh.studentInternshipSupportSystem.VerificationToken.VerificationTokenUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
public class UniversitySupervisorsController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private UniversitySupervisorRepository universitySupervisorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private VerificationTokenUserRepository verificationTokenRepository;

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/university-supervisors/token")
    public ResponseEntity<Object> addUniversitySupervisor(@RequestBody @Valid AddUniversitySupervisor addUniversitySupervisor) {
        UniversitySupervisor universitySupervisor = new UniversitySupervisor(addUniversitySupervisor);
        universitySupervisor.setPassword("BLANK_PASSWORD");
        universitySupervisor.setDepartment(departmentRepository.findById(addUniversitySupervisor.getDepartmentId()).get());

        userService.save(universitySupervisor, RolesName.UNIVERSITY_SUPERVISOR.getRoleName());

        eventPublisher.publishEvent(new UniversitySupervisorAddedEvent(
                this,
                universitySupervisor,
                tokenGenerator.generateAuthToken(universitySupervisor)));

        return ResponseEntity.ok().body("\"status\": \"university supervisor added\"");
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/university-supervisors/token/resend/{id}")
    public ResponseEntity<Object> resendActivationToken(@PathVariable Long id) {
        UniversitySupervisor universitySupervisor = (UniversitySupervisor) userRepository.findById(id).get();

        String generatedToken = verificationTokenRepository
                .findVerificationTokenByUser(universitySupervisor)
                .getToken();

        eventPublisher.publishEvent(new UniversitySupervisorAddedEvent(
                this,
                universitySupervisor,
                generatedToken
        ));

        return ResponseEntity.ok("\"status\": \"university supervisor's activation token resend\"");
    }

    @PostMapping(value="/activate-university-supervisor")
    public ResponseEntity<Object> activateUniversitySupervisor(@RequestBody @Validated ActivateUniversitySupervisor activateUniversitySupervisor,
                                                               @RequestParam String token) {
        UniversitySupervisor universitySupervisorToBeUpdated = universitySupervisorRepository
                .findUniversitySupervisorByEmail(activateUniversitySupervisor.getEmail());

        if (universitySupervisorToBeUpdated == null){
            return ResponseEntity.badRequest().body("\"status\": \"email is incorrect\"");
        }

        VerificationTokenUser verificationToken = verificationTokenRepository.findVerificationTokenByToken(token);

        if (verificationToken == null){
            return ResponseEntity.badRequest().body("\"status\": \"token is incorrect\"");
        }
        if (!verificationToken.getUser().getId().equals(universitySupervisorToBeUpdated.getId())){
            return ResponseEntity.badRequest().body("\"status\": \"token or email is incorrect\"");
        }
        if (!activateUniversitySupervisor.getPassword().equals(activateUniversitySupervisor.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("\"status\": \"passwords are different\"");
        }

        universitySupervisorToBeUpdated.setPassword(activateUniversitySupervisor.getPassword());
        universitySupervisorToBeUpdated.setUsername(activateUniversitySupervisor.getUsername());
        universitySupervisorToBeUpdated.setActive(true);

        userService.save(universitySupervisorToBeUpdated, RolesName.UNIVERSITY_SUPERVISOR.getRoleName());
        verificationTokenRepository.delete(verificationToken);

        return ResponseEntity.ok().body("\"status\": \"university supervisor activated\"");
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/university-supervisors/{id}")
    public ResponseEntity<Object> editUniversitySupervisor(@RequestBody @Valid AddUniversitySupervisor addUniversitySupervisor,
                                                           @PathVariable Long id) {
        if (!((UniversitySupervisor)userRepository.findById(id).get()).isActive()) {
            return ResponseEntity.badRequest().body("\"status\": \"university supervisor not active\"");
        }

        userService.editUniversitySupervisor(addUniversitySupervisor, id);
        return ResponseEntity.ok("\"status\": \"university supervisor updated\"");
    }

    @PreAuthorize("hasAnyAuthority('admin', 'student', 'university_supervisor', 'company_supervisor')")
    @GetMapping("/university-supervisors")
    public ResponseEntity<Object> getAllUniversitySupervisors(){
        return ResponseEntity.ok().body(universitySupervisorRepository.findAll());
    }

    @PreAuthorize("hasAnyAuthority('admin', 'student', 'university_supervisor', 'company_supervisor')")
    @GetMapping("/university-supervisors/{id}")
    public ResponseEntity<Object> getSpecificUniversitySupervisor(@PathVariable Long id) {
        return ResponseEntity.ok(universitySupervisorRepository.findById(id).get());
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/university-supervisors/{id}")
    public ResponseEntity<Object> deleteSpecificUniversitySupervisor(@PathVariable Long id) {
        universitySupervisorRepository.delete(universitySupervisorRepository.findById(id).get());
        return ResponseEntity.ok("\"status\": \"university supervisor deleted\"");
    }

}
