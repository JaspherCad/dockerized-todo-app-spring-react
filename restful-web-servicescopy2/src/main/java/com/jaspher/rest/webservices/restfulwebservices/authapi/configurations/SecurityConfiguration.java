package com.jaspher.rest.webservices.restfulwebservices.authapi.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(
        JwtAuthenticationFilter jwtAuthenticationFilter,
        AuthenticationProvider authenticationProvider
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf()
        .disable()
        .authorizeHttpRequests()
        .antMatchers("/ws/**").permitAll()  // Allow WebSocket connections without authentication

        // Public access for authentication endpoints
        .antMatchers("/auth/**").permitAll()
        // Public access for viewing todos via shareable links
        .antMatchers(HttpMethod.GET, "/share/**/containers/todos", "/share/**/container/todos/**").permitAll()
        // Require authentication for modifying todos via shareable links
        .antMatchers(HttpMethod.POST, "/share/**/containers/todos").authenticated()
        .antMatchers(HttpMethod.PUT, "/share/**/container/todos/**").authenticated()
        .antMatchers(HttpMethod.DELETE, "/share/**/container/todos/**").authenticated()
        // Public access for shareable links (allow GET requests without login)
        .antMatchers(HttpMethod.GET, "/users/{username}/todos/**").permitAll()
        .antMatchers(HttpMethod.PUT, "/share/**/container/todos/**/comments").authenticated()
        // Require authentication for modifying todos via username
        .antMatchers(HttpMethod.POST, "/users/{username}/todos/**").authenticated()
        .antMatchers(HttpMethod.PUT, "/users/{username}/todos/**").authenticated()
        .antMatchers(HttpMethod.DELETE, "/users/{username}/todos/**").authenticated()
        // Allow H2 console access (use with caution in production)
        .antMatchers("/h2-console/**").permitAll()
        // All other requests require authentication
                .anyRequest().authenticated()  // All other requests require authentication
                .and()
                .cors()  // Enable CORS
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.headers().frameOptions().disable();

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",configuration);

        return source;
    }
}

