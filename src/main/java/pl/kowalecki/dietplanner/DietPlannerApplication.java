package pl.kowalecki.dietplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DietPlannerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DietPlannerApplication.class, args);
    }
}