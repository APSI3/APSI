package apsi.team3.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableJpaRepositories(
    basePackages = {
        "apsi.team3.backend.repository"
    }
)
@EnableWebSecurity
public class Config {
    private final ApsiAuthenticationProvider authenticationProvider;

    @Autowired
    public Config(ApsiAuthenticationProvider authenticationProvider){
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(c -> c.disable())
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(r -> r.requestMatchers("/user/login").anonymous())
            .authorizeHttpRequests(r -> r.anyRequest().authenticated())
            .httpBasic(c -> c.authenticationEntryPoint((req, res, authEx) -> {
                if (!req.getRequestURI().contains("login"))
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            }))
            .httpBasic(Customizer.withDefaults())
            .build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        return authenticationManagerBuilder.build();
    }
}
