package ensfgroup11.acmeplex.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
// import ensfgroup11.acmeplex.service.AuthenticationUserRetrievalService; // Uncomment when ready to use UserDetailsService
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // @Autowired
    // private AuthenticationUserRetrievalService userDetailsService; // Uncomment when ready to use UserDetailsService

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login", "/register", "/h2-console/**").permitAll() // Allow registration, login, H2 console access
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Custom login page
                        .defaultSuccessUrl("/home", true) // Redirect to /home after successful login
                        .permitAll() // Allow all users to access the login page
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Default logout URL
                        .logoutSuccessUrl("/logout-success") // Redirect to logout success page
                        .permitAll() // Allow all users to log out
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**") // Optional: Disable CSRF protection for H2 console
                );

        return http.build(); // Build the security filter chain
    }

    // In-memory authentication configuration (current active setup)
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles("USER") // User credentials without encoding
                .and()
                .withUser("admin").password("{noop}admin").roles("ADMIN"); // Admin credentials without encoding
    }

    // Database-based authentication configuration (future setup)
    // Uncomment the following method and related UserDetailsService injection when ready

    // @Autowired
    // public void configure(AuthenticationManagerBuilder auth) throws Exception {
    //     auth.userDetailsService(userDetailsService);
    //     // Optionally add a password encoder here if you're using one
    //     // .passwordEncoder(passwordEncoder());
    // }

    // Define a password encoder if using encoded passwords
    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }
}

