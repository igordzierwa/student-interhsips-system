package agh.studentInternshipSupportSystem;

import agh.studentInternshipSupportSystem.Authentication.JwtConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class StudentInternshipSupportSystemBackendApplication {

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtConfig jwtConfig() {
		return new JwtConfig();
	}

	public static void main(String[] args) {
		SpringApplication.run(StudentInternshipSupportSystemBackendApplication.class, args);
	}

}
