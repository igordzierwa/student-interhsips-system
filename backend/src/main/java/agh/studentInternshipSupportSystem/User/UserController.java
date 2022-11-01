package agh.studentInternshipSupportSystem.User;

import agh.studentInternshipSupportSystem.Authentication.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
@CrossOrigin
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtConfig jwtConfig;

    @GetMapping(value = "/logged/session")
    @ResponseBody
    public ResponseEntity currentUserNameSimple(HttpServletRequest request) {
        String header = request.getHeader(jwtConfig.getHeader());
        if (header != null)
            return getResponseSession(header);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("not logged");
    }

    private ResponseEntity getResponseSession(String header) {
        LoggedUserDTO loggedUserDTO = jwtConfig.getResponseSession(header);
        if(loggedUserDTO != null){
            return ResponseEntity.status(HttpStatus.OK).body(loggedUserDTO);
        } else {
            log.info("Error");
            return (ResponseEntity<Set<String>>) ResponseEntity.status(HttpStatus.UNAUTHORIZED);
        }
    }

    @PreAuthorize("hasAnyAuthority('admin', 'company_supervisor', 'university_supervisor', 'student')")
    @GetMapping("/users")
    public ResponseEntity<Object> listUsers() {
        return ResponseEntity.ok().body(userRepository.findAll());
    }
}
