package apsi.team3.backend.config;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableJpaRepositories(
        basePackages = {
                "apsi.team3.backend.repository"
        }
)

public class Config {
}
