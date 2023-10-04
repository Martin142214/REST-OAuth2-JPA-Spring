package com.example.RestOAuth2JPA;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.example.RestOAuth2JPA.DTO.repositories.auth.IUsersRepository;
import com.example.RestOAuth2JPA.components.classModels.AuthToken;
import com.example.RestOAuth2JPA.services.userDetails.CustomUserDetails;
import com.example.RestOAuth2JPA.services.userDetails.UserDetailsServiceImplementation;


@Configuration
@EnableWebSecurity
@EnableJpaRepositories
//@ConfigurationProperties(prefix = "auth")
@EnableTransactionManagement
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration{

    //@Autowired
    //private JwtRequestFilter jwtRequestFilter;

    @Autowired UserDetailsServiceImplementation customUserDetailsServiceImplementation;

    /*@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        final List<GlobalAuthenticationConfigurerAdapter> configurers = new ArrayList<>();
        configurers.add(new GlobalAuthenticationConfigurerAdapter() {
                    @Override
                    public void configure(AuthenticationManagerBuilder auth) throws Exception {
                        auth.userDetailsService(customUserDetailsServiceImplementation).passwordEncoder(passwordEncoder());
                    }
                }
        );
        return authConfig.getAuthenticationManager();
    }*/
    

    
    /*protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsServiceImplementation);
    }*/
    

    /*@Bean
    protected AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        return 
    }*/


    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
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
                .and().csrf().disable();*/

        http.sessionManagement(session -> session.maximumSessions(1).maxSessionsPreventsLogin(true));
        http.csrf().disable()
                    .authorizeRequests()
                    .requestMatchers("/user/register").permitAll()
                    .requestMatchers("/admin/register").permitAll()
                    .requestMatchers("/scripts/**").permitAll()
                    .requestMatchers("/css/**").permitAll()
                    .requestMatchers("/images/**").permitAll()
                    //.requestMatchers("/user/register/second").permitAll()
                    //.requestMatchers("/api/v1/roles").anonymous()
                    .anyRequest().authenticated()
                    .and().formLogin().loginPage("/user/login").defaultSuccessUrl("/user/checkPath").permitAll()
                    .and().logout().logoutSuccessUrl("/user/login").deleteCookies("JSESSIONID");
        //http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
                

        return http.build();
    }
}
