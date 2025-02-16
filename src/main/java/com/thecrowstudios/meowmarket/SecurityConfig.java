package com.thecrowstudios.meowmarket;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.thecrowstudios.meowmarket.authentication.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
        @Autowired
        private CustomUserDetailsService userService;

        @Autowired
        private DataSource dataSource;

        @Value("${app.remember-me.secret}")
        private String rememberMeSecret;

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
                return authConfig.getAuthenticationManager();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http)
                        throws Exception {
                http.csrf(csrf -> csrf.disable()) // TODO - not recommended for production?
                                .authorizeHttpRequests(
                                                auth -> auth
                                                                .requestMatchers("/listings/new", "/listings/create",
                                                                                "/listings/edit", "/actuator/**")
                                                                .hasRole("ADMIN")
                                                                .anyRequest().permitAll())
                                .formLogin(login -> login.loginPage("/api/auth/login")
                                                .loginProcessingUrl("/api/auth/login")
                                                .defaultSuccessUrl("/")
                                                .failureUrl("/api/auth/login?error=true")
                                                .permitAll())
                                .rememberMe(rememberMe -> rememberMe
                                .rememberMeServices(rememberMeServices()))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                                                .invalidSessionUrl("/api/auth/login")
                                                .maximumSessions(1)
                                                .expiredUrl("/api/auth/login"))
                                .logout(logout -> logout
                                                .logoutUrl("/api/auth/logout")
                                                .logoutSuccessUrl("/api/auth/login?logout=true")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID"))
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint((request, response, authException) -> {
                                                        response.sendRedirect("/api/auth/login");
                                                })
                                                .accessDeniedHandler(
                                                                (request, response, accessDeniedException) -> response
                                                                                .sendRedirect("/api/auth/login")))
                                .anonymous(anonymous -> anonymous.disable()); // breaks cart sometimes if we dont do
                                                                              // this
                return http.build();
        }

        @Bean
        public PersistentTokenRepository persistentTokenRepository() {
                JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
                tokenRepository.setDataSource(dataSource);
                // Uncomment the line below if you want Spring to create the table automatically
                // tokenRepository.setCreateTableOnStartup(true);
                return tokenRepository;
        }

        @Bean
        public PasswordEncoder getPasswordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public HttpSessionEventPublisher httpSessionEventPublisher() {
                return new HttpSessionEventPublisher();
        }

        @Bean
        public RememberMeServices rememberMeServices() {
                PersistentTokenBasedRememberMeServices services = new PersistentTokenBasedRememberMeServices(
                                rememberMeSecret, userService, persistentTokenRepository());
                services.setParameter("remember-me");
                services.setCookieName("remember-me");

                return services;
        }
}