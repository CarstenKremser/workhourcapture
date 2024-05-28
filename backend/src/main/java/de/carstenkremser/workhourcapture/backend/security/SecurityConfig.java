package de.carstenkremser.workhourcapture.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    final private String[] PUBLIC_URLS = new String[]{
            "/api/user/login",
            "/api/user/logout",
            "/api/user/register"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        requestAttributeHandler.setCsrfRequestAttributeName(null);

        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(requestAttributeHandler)
                        .ignoringRequestMatchers(PUBLIC_URLS)
                )
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/api/user/register").permitAll();
                    request.requestMatchers("/api/workingtime").authenticated();
                    request.requestMatchers("/api/timerecord").authenticated();
                    request.anyRequest().permitAll();
                })
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .httpBasic(c -> {
                    c.authenticationEntryPoint(((request, response, authException) ->
                            response.sendError(
                                    HttpStatus.UNAUTHORIZED.value(),
                                    HttpStatus.UNAUTHORIZED.getReasonPhrase())));
                    c.init(http);
                });
        return http.build();
    }
}
