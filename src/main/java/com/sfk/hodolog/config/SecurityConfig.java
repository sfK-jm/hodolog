package com.sfk.hodolog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfk.hodolog.config.handler.Http401Handler;
import com.sfk.hodolog.config.handler.Http403Handler;
import com.sfk.hodolog.config.handler.LoginFailHandler;
import com.sfk.hodolog.domain.Users;
import com.sfk.hodolog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;


@Slf4j
@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/error")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers(PathRequest.toH2Console());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserRepository userRepository) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/signup").permitAll()
                        .requestMatchers("/user").hasAnyRole("USER")
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(configurer -> configurer
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/")
                        .failureHandler(new LoginFailHandler(objectMapper))
                )
                .exceptionHandling(e -> {
                    e.accessDeniedHandler(new Http403Handler(objectMapper));
                    e.authenticationEntryPoint(new Http401Handler(objectMapper));
                })
                .rememberMe(rm -> rm
                        .rememberMeParameter("remember")
                        .alwaysRemember(false)
                        .tokenValiditySeconds(86400 * 30)
                )
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            Users users = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException(username + "을 찾을 수 없습니다."));

            return new UserPrincipal(users);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new SCryptPasswordEncoder(16, 8, 1, 32, 64);
    }
}
