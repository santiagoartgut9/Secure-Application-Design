package com.arep.demo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtUtil jwtUtil = new JwtUtil(jwtSecret);
        JwtFilter jwtFilter = new JwtFilter(jwtUtil);

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // permitir recursos estáticos en ubicaciones comunes (css, js, images, webjars, favicon...)
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                // permitir la página raíz, archivo principal de JS y otros fijos
                .requestMatchers("/", "/index.html", "/app.js", "/favicon.ico", "/static/**").permitAll()
                // endpoints públicos
                .requestMatchers("/api/auth/**", "/h2-console/**").permitAll()
                // todo lo demás autenticado
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // permitir render de frames para H2 console
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}
