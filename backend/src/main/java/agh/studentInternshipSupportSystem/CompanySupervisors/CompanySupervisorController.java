package agh.studentInternshipSupportSystem.CompanySupervisors;

import agh.studentInternshipSupportSystem.Companies.Company;
import agh.studentInternshipSupportSystem.Companies.CompanyRepository;
import agh.studentInternshipSupportSystem.CompanySupervisors.EventListener.CompanySupervisorAddedEvent;
import agh.studentInternshipSupportSystem.CompanySupervisors.RequestBody.AddCompanySupervisor;
import agh.studentInternshipSupportSystem.Roles.RolesName;
import agh.studentInternshipSupportSystem.UniversitySupervisors.RequestBody.ActivateUniversitySupervisor;
import agh.studentInternshipSupportSystem.User.User;
import agh.studentInternshipSupportSystem.User.UserRepository;
import agh.studentInternshipSupportSystem.User.UserService;
import agh.studentInternshipSupportSystem.VerificationToken.TokenGenerator;
import agh.studentInternshipSupportSystem.VerificationToken.VerificationTokenUser;
import agh.studentInternshipSupportSystem.VerificationToken.VerificationTokenRepository;
import agh.studentInternshipSupportSystem.VerificationToken.VerificationTokenUserRepository;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@Slf4j
public class CompanySupervisorController {
    @Autowired
    private CompanySupervisorRepository companySupervisorRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private VerificationTokenUserRepository verificationTokenRepository;

    @Autowired
    private TokenGenerator tokenGenerator;

    @PreAuthorize("hasAnyAuthority('admin', 'student', 'university_supervisor')")
    @PostMapping("/company-supervisors/token")
    public ResponseEntity<Object> addCompanySupervisor(@RequestBody @Valid AddCompanySupervisor addCompanySupervisor) {
        CompanySupervisor companySupervisor = new CompanySupervisor(addCompanySupervisor);
        companySupervisor.setPassword("BLANK_PASSWORD");
        companySupervisor.setCompany(companyRepository.findById(addCompanySupervisor.getCompanyId()).get());

        userService.save(companySupervisor, RolesName.COMPANY_SUPERVISOR.getRoleName());

        eventPublisher.publishEvent(new CompanySupervisorAddedEvent(
                this,
                companySupervisor,
                tokenGenerator.generateAuthToken(companySupervisor)));

        return ResponseEntity.ok().body("\"status\": \"company supervisor added\"");
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/company-supervisors/token/resend/{id}")
    public ResponseEntity<Object> resendActivationToken(@PathVariable Long id) {
        CompanySupervisor companySupervisor = (CompanySupervisor) userRepository.findById(id).get();

        String generatedToken = verificationTokenRepository
                .findVerificationTokenByUser(companySupervisor)
                .getToken();

        eventPublisher.publishEvent(new CompanySupervisorAddedEvent(
                this,
                companySupervisor,
                generatedToken
        ));

        return ResponseEntity.ok("\"status\": \"company supervisor's activation token resend\"");
    }

    @PostMapping(value="/activate-company-supervisor")
    public ResponseEntity<Object> activateCompanySupervisor(@RequestBody @Valid ActivateUniversitySupervisor activateUniversitySupervisor,
                                                               @RequestParam String token) {
        CompanySupervisor companySupervisorToBeUpdated = companySupervisorRepository
                .findCompanySupervisorByEmail(activateUniversitySupervisor.getEmail());

        if (companySupervisorToBeUpdated == null) {
            return ResponseEntity.badRequest().body("\"status\": \"email is incorrect\"");
        }

        VerificationTokenUser verificationToken = verificationTokenRepository.findVerificationTokenByToken(token);

        if (verificationToken == null){
            return ResponseEntity.badRequest().body("\"status\": \"token is incorrect\"");
        }
        if (!verificationToken.getUser().getId().equals(companySupervisorToBeUpdated.getId())){
            return ResponseEntity.badRequest().body("\"status\": \"token or email is incorrect\"");
        }
        if (!activateUniversitySupervisor.getPassword().equals(activateUniversitySupervisor.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("\"status\": \"passwords are different\"");
        }

        companySupervisorToBeUpdated.setPassword(activateUniversitySupervisor.getPassword());
        companySupervisorToBeUpdated.setUsername(activateUniversitySupervisor.getUsername());
        companySupervisorToBeUpdated.setActive(true);

        userService.save(companySupervisorToBeUpdated, RolesName.COMPANY_SUPERVISOR.getRoleName());
        verificationTokenRepository.delete(verificationToken);

        return ResponseEntity.ok().body("\"status\": \"company supervisor activated\"");
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/company-supervisors/{id}")
    public ResponseEntity<Object> editCompanySupervisor(@RequestBody @Valid AddCompanySupervisor addCompanySupervisor,
                                                        @PathVariable Long id) {
        if (!((CompanySupervisor)userRepository.findById(id).get()).isActive()) {
            return ResponseEntity.badRequest().body("\"status\": \"company supervisor not active\"");
        }

        userService.editCompanySupervisor(addCompanySupervisor, id);
        return ResponseEntity.ok("\"status\": \"company supervisor updated\"");
    }

    @PreAuthorize("hasAnyAuthority('admin', 'student', 'university_supervisor', 'company_supervisor')")
    @GetMapping("/company-supervisors")
    public ResponseEntity<Object> getAllCompanySupervisors(){
        return ResponseEntity.ok().body(companySupervisorRepository.findAll());
    }

    @PreAuthorize("hasAnyAuthority('admin', 'student', 'university_supervisor', 'company_supervisor')")
    @GetMapping("/company-supervisors/{id}")
    public ResponseEntity<Object> getSpecificCompanySupervisor(@PathVariable Long id) {
        return ResponseEntity.ok(companySupervisorRepository.findById(id).get());
    }

    @PreAuthorize("hasAnyAuthority('admin', 'student', 'university_supervisor', 'company_supervisor')")
    @GetMapping("/company-supervisors/company/{id}")
    public ResponseEntity<Object> getCompanySupervisorsFromDefinedCompany(@PathVariable Long id) throws NotFoundException {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        return ResponseEntity.ok(companySupervisorRepository.findCompanySupervisorsByCompany(company));
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/company-supervisors/{id}")
    public ResponseEntity<Object> deleteSpecificCompanySupervisor(@PathVariable Long id) {
        companySupervisorRepository.delete(companySupervisorRepository.findById(id).get());
        return ResponseEntity.ok("\"status\": \"company supervisor deleted\"");
    }
}
