package vod.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableMethodSecurity
public class VodSecurityConfig {

    @Bean
    UserDetailsService userDetailsService(DataSource dataSource) {

        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
        userDetailsManager.setDataSource(dataSource);
        userDetailsManager.setUsersByUsernameQuery("select username, password, 'true' from user where username=?");
        userDetailsManager.setAuthoritiesByUsernameQuery("select username, role from role where username=?");

        return userDetailsManager;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf( csrfConfigurer -> csrfConfigurer.disable() );

        http.authorizeHttpRequests(registry->registry
                //.requestMatchers(HttpMethod.POST, "/movies").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/cinemas").authenticated()
                .anyRequest().permitAll()
        );

        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }

}
