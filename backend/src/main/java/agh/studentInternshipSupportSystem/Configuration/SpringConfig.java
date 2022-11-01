package agh.studentInternshipSupportSystem.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class SpringConfig {
    @Bean
    public ExecutorService getExecutorService(){
        ExecutorService executor = Executors.newWorkStealingPool();
        return executor;
    }
}
