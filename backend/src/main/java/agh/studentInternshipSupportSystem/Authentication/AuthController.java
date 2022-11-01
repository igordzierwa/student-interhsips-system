package agh.studentInternshipSupportSystem.Authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
public class AuthController {
    @PostMapping(value = "/auth", consumes={"multipart/form-data"})
    public ResponseEntity getBearerToken(@Valid AuthData authData) {
        return ResponseEntity.ok().build();
    }
}
