package ru.azamatkomaev.storage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.azamatkomaev.storage.advice.FilterChainExceptionHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final FilterChainExceptionHandler filterChainExceptionHandler;

    @Autowired
    public SecurityConfiguration(JwtAuthFilter jwtAuthFilter, AuthenticationProvider authenticationProvider, FilterChainExceptionHandler filterChainExceptionHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.filterChainExceptionHandler = filterChainExceptionHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // requestMatchers do not work here. So they are moved to jwtAuthFilter.
        return http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(filterChainExceptionHandler, jwtAuthFilter.getClass())
            .authenticationProvider(authenticationProvider)
            .build();
    }
}
