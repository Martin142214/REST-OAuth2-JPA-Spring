package com.example.RestOAuth2JPA;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.RestOAuth2JPA.DTO.entities.classModels.AuthToken;
import com.example.RestOAuth2JPA.filters.JwtRequestFilter;
import com.example.RestOAuth2JPA.services.UserDetailsServiceImplementation;


@Configuration
@ConfigurationProperties(prefix = "auth")
@EnableWebSecurity
public class SecurityConfiguration{

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserDetailsServiceImplementation myUserDetailsServiceImplementation;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        final List<GlobalAuthenticationConfigurerAdapter> configurers = new ArrayList<>();
        configurers.add(new GlobalAuthenticationConfigurerAdapter() {
                    @Override
                    public void configure(AuthenticationManagerBuilder auth) throws Exception {
                        auth.userDetailsService(myUserDetailsServiceImplementation).passwordEncoder(passwordEncoder());
                    }
                }
        );
        return authConfig.getAuthenticationManager();
    }
    

   /* protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.parentAuthenticationManager(authenticationManager).userDetailsService(myUserDetailsServiceImplementation);
    }*/
    

    /*@Bean
    protected AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        return 
    }*/

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /*http
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login.html").permitAll()
                .loginProcessingUrl("/authenticate").permitAll()
                .defaultSuccessUrl("/home", true)
                .and()
                .rememberMe()
                .alwaysRemember(true)
                .tokenValiditySeconds(30 * 6)
                .rememberMeCookieName("auth")
                .key(authToken.getSecret_key())
                .and().csrf().disable();

        return http.build();*/
        http.csrf().disable()
                    .authorizeRequests().requestMatchers("/authenticate").permitAll()
                    .requestMatchers(HttpMethod.GET,"/login").permitAll()
                    .anyRequest().authenticated()
                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
                

        return http.build();
    }
}
