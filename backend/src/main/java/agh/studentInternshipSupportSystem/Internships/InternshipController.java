package agh.studentInternshipSupportSystem.Internships;

import agh.studentInternshipSupportSystem.Authentication.JwtConfig;
import agh.studentInternshipSupportSystem.Companies.CompanyRepository;
import agh.studentInternshipSupportSystem.CompanySupervisors.CompanySupervisor;
import agh.studentInternshipSupportSystem.CompanySupervisors.CompanySupervisorRepository;
import agh.studentInternshipSupportSystem.Exceptions.RecordNotFoundException;
import agh.studentInternshipSupportSystem.Internships.EventListener.InternshipAcceptedByCompanySupervisor;
import agh.studentInternshipSupportSystem.Internships.EventListener.InternshipAcceptedByStudent;
import agh.studentInternshipSupportSystem.Internships.EventListener.InternshipAcceptedByUniversitySupervisor;
import agh.studentInternshipSupportSystem.Internships.RequestBody.AcceptInternshipByCompanySupervisor;
import agh.studentInternshipSupportSystem.Internships.RequestBody.AcceptInternshipByUniversitySupervisor;
import agh.studentInternshipSupportSystem.Internships.RequestBody.CreateInternship;
import agh.studentInternshipSupportSystem.Internships.RequestBody.EditStudentFields;
import agh.studentInternshipSupportSystem.Roles.RoleRepository;
import agh.studentInternshipSupportSystem.Roles.RolesName;
import agh.studentInternshipSupportSystem.Students.Student;
import agh.studentInternshipSupportSystem.Students.StudentRepository;
import agh.studentInternshipSupportSystem.UniversitySupervisors.EventListener.UniversitySupervisorAddedEvent;
import agh.studentInternshipSupportSystem.UniversitySupervisors.RequestBody.ActivateUniversitySupervisor;
import agh.studentInternshipSupportSystem.User.LoggedUserDTO;
import agh.studentInternshipSupportSystem.User.User;
import agh.studentInternshipSupportSystem.User.UserRepository;
import agh.studentInternshipSupportSystem.VerificationToken.*;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class InternshipController {
    @Autowired
    private InternshipRepository internshipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CompanyRepository  companyRepository;

    @Autowired
    CompanySupervisorRepository companySupervisorRepository;

    @Autowired
    JwtConfig jwtConfig;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    VerificationTokenInternshipCSRepository verificationTokenInternshipCSRepository;

    @Autowired
    VerificationTokenInternshipUSRepository verificationTokenInternshipUSRepository;

    @GetMapping(value = "/internship")
    @PreAuthorize("hasAuthority('student')")
    public ResponseEntity<Object> getStudentInternship(@RequestParam String username){
        User user = userRepository.findByUsername(username);
        Internship internship;
        if (user == null) {
            return ResponseEntity.noContent().build();
        }
        if (user.getRoles().contains(roleRepository.findByName(RolesName.STUDENT.getRoleName()))) {
            internship = internshipRepository.findByStudentUsername(username);
        } else {
            return ResponseEntity.noContent().build();
        }
        if (internship == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(internship);
    }

    @PostMapping(value = "/internship/create")
    @PreAuthorize("hasAuthority('student')")
    public ResponseEntity<Object> createInternship(@RequestBody CreateInternship createInternship,  HttpServletRequest request){
        String header = request.getHeader(jwtConfig.getHeader());
        LoggedUserDTO loggedUserDTO;
        if (header != null) {
            loggedUserDTO = jwtConfig.getResponseSession(header);
        } else {
            throw new RecordNotFoundException( "No user defined");
        }

        log.info("internship creartion {}", createInternship.toString());

        Student student = studentRepository.findStudentByUsername(loggedUserDTO.getUsername());
        CompanySupervisor companySupervisor = companySupervisorRepository.findCompanySupervisorById(createInternship.getCompanySupervisorId());
        if(student == null || companySupervisor == null ||
                !companySupervisor.getCompany().getId().equals(createInternship.getCompanyId()) ||
                student.getStudentGroup() == null
        ){
            log.info("here");
            return ResponseEntity.badRequest().build();
        }
        if (internshipRepository.findByStudentUsername(student.getUsername()) != null){
            return ResponseEntity.badRequest().build();
        }
        Internship internship = new Internship();
        internship.setStudent(student);
        internship.setCompanySupervisor(companySupervisor);
        internship.setStudentAddress(createInternship.getAddress());
        internship.setStudentPosition(createInternship.getPosition());
        internship.setFieldOfStudy(createInternship.getFieldOfStudy());
        internship.setIsPaid(createInternship.getPayment());
        internship.setNumber_index(createInternship.getNumber_index());
        internship.setStudentAddress(createInternship.getAddress());
        internshipRepository.save(internship);


        eventPublisher.publishEvent(new InternshipAcceptedByStudent(
                this,
                internship));

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/internship/resend-email")
    @PreAuthorize("hasAuthority('student')")
    public ResponseEntity<Object> resendInternshipAcceptance(HttpServletRequest request) {
        String header = request.getHeader(jwtConfig.getHeader());
        LoggedUserDTO loggedUserDTO;
        if (header != null) {
            loggedUserDTO = jwtConfig.getResponseSession(header);
        } else {
            throw new RecordNotFoundException("No user defined");
        }

        Internship internship = internshipRepository.findByStudentUsername(loggedUserDTO.getUsername());
        if(!internship.isCompanySupervisorAccepted()){

            eventPublisher.publishEvent(new InternshipAcceptedByStudent(
                    this,
                    internship));

            return ResponseEntity.ok("{\"status\": \"succesfully resend CS token\"}");
        }

        if(!internship.isUniversitySupervisorAccepted()){
            eventPublisher.publishEvent(new InternshipAcceptedByCompanySupervisor(
                    this,
                    internship));


            return ResponseEntity.ok("{\"status\": \"succesfully resend US token\"}");
        }

        throw new IllegalArgumentException("Internship not started or already accepted");

    }

    @GetMapping(value = "/internship/{id}")
    public ResponseEntity<Object> getSingleInternship(@PathVariable Long id){
        return ResponseEntity.ok(internshipRepository.findById(id).get());
    }

    @PostMapping(value="/internship/{id}/edit-student-fields")
    @PreAuthorize("hasAuthority('student')")
    public ResponseEntity<Object> editStudentFields(@PathVariable Long id, @RequestBody EditStudentFields editStudentFields){
        Optional<Internship> internshipOpt = internshipRepository.findById(id);

        if(internshipOpt.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        Internship internship = internshipOpt.get();
        internship.setDateStarted(editStudentFields.getDateStarted());
        internship.setDateFinished(editStudentFields.getDateFinished());
        internship.setHoursCount(editStudentFields.getHoursCount());
        internship.setReport(editStudentFields.getReport());
        internship.setEditionNotAllowed(true);
        internshipRepository.save(internship);
        return ResponseEntity.ok().build();
    }


    @GetMapping(value="/accept-internship/supervisor")
    public ResponseEntity<Object> acceptInternshipGetDataCS(@RequestParam String token) {
        log.info("sdasdaadsadsd");
        VerificationTokenInternshipCS tok = verificationTokenInternshipCSRepository.findVerificationTokenByToken(token);
        VerificationTokenInternshipUS tok1 = verificationTokenInternshipUSRepository.findVerificationTokenByToken(token);
        if(tok == null)
            return ResponseEntity.ok(tok1);

        return ResponseEntity.ok(tok);
    }


    @PostMapping(value="/accept-internship/company-supervisor")
    public ResponseEntity<Object> acceptInternship(@RequestBody @Valid AcceptInternshipByCompanySupervisor acceptInternshipByCompanySupervisor) {
        VerificationTokenInternshipCS token = verificationTokenInternshipCSRepository.findVerificationTokenByToken(acceptInternshipByCompanySupervisor.getToken());

        Internship internship = token.getInternship();

        if(!internship.isEditionNotAllowed() || internship.isCompanySupervisorAccepted())
            throw new IllegalArgumentException("Internship already accepted or not ready to accept");


        internship.setGradesFromCompanySupervisor(acceptInternshipByCompanySupervisor.getGrade());
        internship.setAdditionalInformation(acceptInternshipByCompanySupervisor.getReview());
        internship.setCompanySupervisorAccepted(true);
        internshipRepository.save(internship);


        eventPublisher.publishEvent(new InternshipAcceptedByCompanySupervisor(
                this,
                internship));

        verificationTokenInternshipCSRepository.delete(token);
        return ResponseEntity.ok(token);
    }


    @PostMapping(value="/accept-internship/university-supervisor")
    public ResponseEntity<Object> acceptInternshipUniversity(@RequestBody @Valid AcceptInternshipByUniversitySupervisor acceptInternshipByUniversitySupervisor) {
        VerificationTokenInternshipUS token = verificationTokenInternshipUSRepository.findVerificationTokenByToken(acceptInternshipByUniversitySupervisor.getToken());

        Internship internship = token.getInternship();

        if(!internship.isEditionNotAllowed() || internship.isUniversitySupervisorAccepted())
            throw new IllegalArgumentException("Internship already accepted or not ready to accept");


        internship.setGradeFromUniversitySupervisor(acceptInternshipByUniversitySupervisor.getGrade());
        internship.setUniversitySupervisorAccepted(true);
        internshipRepository.save(internship);

        eventPublisher.publishEvent(new InternshipAcceptedByUniversitySupervisor(
                this,
                internship));
        verificationTokenInternshipUSRepository.delete(token);
        return ResponseEntity.ok(token);
    }

    @GetMapping(value="/internship/get-verification-tokens")
    public ResponseEntity<Object> getVerificationTokens(@RequestParam String username){
        List<VerificationTokenInternshipCS> csTokens =
                verificationTokenInternshipCSRepository.findVerificationTokenInternshipCSByCompanySupervisor_Username(username);

        List<VerificationTokenInternshipUS> usTokens =
                verificationTokenInternshipUSRepository.findVerificationTokenInternshipUSByUniversitySupervisor_Username(username);

        if(csTokens.isEmpty() && usTokens.isEmpty()){
            return ResponseEntity.noContent().build();
        } else if(!csTokens.isEmpty()){
            return ResponseEntity.ok(csTokens);
        } else {
            return ResponseEntity.ok(usTokens);
        }
    }

}

