package agh.studentInternshipSupportSystem.Configuration;

import agh.studentInternshipSupportSystem.Roles.RoleService;
import agh.studentInternshipSupportSystem.User.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Bean
    CommandLineRunner initDatabase(){
        if(roleService.checkRoles()) {
            roleService.makeRoles();
        }
        userService.addAdminAccIfNotExist();
        return args -> {
            log.info("Database loaded successfully");
        };

    }
}
