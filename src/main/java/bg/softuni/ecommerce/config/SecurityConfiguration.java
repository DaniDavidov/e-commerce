package bg.softuni.ecommerce.config;

import bg.softuni.ecommerce.repository.UserRepository;
import bg.softuni.ecommerce.service.EcommerceUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {
    private UserRepository userRepository;

    @Autowired
    public SecurityConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/", "/users/register", "/users/login", "/offers/all", "/offers/{offerId}/details", "/users/login-error", "/brands/all").permitAll()
                .requestMatchers("/users/all", "/users/toUser/**", "/users/toModerator/**", "/brands/add").hasRole("ADMIN")
                .requestMatchers("/users/blacklist/**").hasRole("ADMIN")
                .requestMatchers("/offers/all/unapproved", "/offers/approve/**").hasRole("ADMIN")
                .requestMatchers("/orders", "/orders/unprocessed", "/orders/{id}/details", "/orders/{id}/confirm").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/users/login")
                .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                .defaultSuccessUrl("/", true)
                .failureForwardUrl("/users/login-error")
                .and()
                // configure logout
                .logout()
                // which is the logout url, must be POST request
                .logoutUrl("/logout")
                // on logout go to the home page
                .logoutSuccessUrl("/")
                // invalidate the session and delete the cookies
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .and()
                .rememberMe()
                .key("uniqueKey")
                .tokenValiditySeconds(604800)
                .userDetailsService(userDetailsService());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new EcommerceUserDetailsService(userRepository);
    }

}
