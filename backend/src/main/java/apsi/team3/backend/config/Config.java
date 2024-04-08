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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import apsi.team3.backend.model.UserType;

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
        return http.csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            // TODO: dodawanie eventu, etc. tylko dla userów reprezentujących firmę
            .authorizeHttpRequests(r -> r.requestMatchers("/user/login").anonymous())
            .authorizeHttpRequests(r -> r.requestMatchers("/event/create").hasAnyAuthority(UserType.SUPERADMIN.toString(), UserType.ORGANIZER.toString()))
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

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET, POST, OPTIONS")
                        .allowedOrigins("http://localhost:3000")
                        .allowCredentials(true);
            }
        };
    }
}
