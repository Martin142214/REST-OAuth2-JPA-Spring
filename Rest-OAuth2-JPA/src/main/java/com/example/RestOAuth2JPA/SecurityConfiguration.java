package com.example.RestOAuth2JPA;

import java.util.Properties;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.RestOAuth2JPA.DTO.entities.classModels.AuthToken;
import com.example.RestOAuth2JPA.services.UserDetailsServiceImplementation;


@Configuration
@ConfigurationProperties(prefix = "auth")
@EnableWebSecurity
public class SecurityConfiguration{

    @Autowired
    private AuthToken authToken;

    @Autowired
    private UserDetailsServiceImplementation myUserDetailsServiceImplementation;
    
    
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsServiceImplementation);
    }

    @Bean
    protected AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests().anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/login.html").permitAll()
            .loginProcessingUrl("/authenticate").permitAll()
            .defaultSuccessUrl("/home", true)
            .and()
            .rememberMe()
            .alwaysRemember(true)
            .tokenValiditySeconds(30*6)
            .rememberMeCookieName("auth")
            .key(authToken.getSecret_key())
            .and().csrf().disable();

            return http.build();
    }
}
