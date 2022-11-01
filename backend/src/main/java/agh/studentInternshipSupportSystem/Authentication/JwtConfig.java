package agh.studentInternshipSupportSystem.Authentication;

import agh.studentInternshipSupportSystem.User.LoggedUserDTO;
import agh.studentInternshipSupportSystem.User.User;
import agh.studentInternshipSupportSystem.User.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter @Setter
@Slf4j
public class JwtConfig {

    @Autowired
    UserRepository userRepository;

    @Value("${security.jwt.uri:/auth/**}")
    private String Uri;

    @Value("${security.jwt.header:Authorization}")
    private String header;

    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;

    @Value("${security.jwt.expiration:#{24*60*60}}")
    private int expiration;

    @Value("${security.jwt.secret:JwtSecretKey}")
    private String secret;


    public LoggedUserDTO getResponseSession(String header) {
        log.info(header);
        String token = header.replace(this.getPrefix(), "");
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(this.getSecret().getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            log.info(username);
            if (username != null) {
                @SuppressWarnings("unchecked")
                List<String> authorities = (List<String>) claims.get("authorities");
                Set<String> roles = authorities.stream().collect(Collectors.toSet());

                log.info("{}", roles);
                LoggedUserDTO loggedUserDTO = new LoggedUserDTO();
                User user = userRepository.findByUsername(username);

                BeanUtils.copyProperties(user, loggedUserDTO);
                loggedUserDTO.setRoles(roles);

                return loggedUserDTO;
            }
        } catch (Exception e) {
            log.info("Error" + e.getMessage());
            return null;
        }
        return null;
    }
}