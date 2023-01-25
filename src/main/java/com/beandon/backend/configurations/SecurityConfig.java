package com.beandon.backend.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${auth.password}")
    private String password;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/health").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/plants").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/plants/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/stocks").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/stocks/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/transactions").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/transactions/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/account").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/account/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/previews").permitAll();
        http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
        http.csrf().disable();

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User
                .withUsername("admin")
                .password(password)
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
