package com.example.bankapp.config;


import com.example.bankapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    AccountService accountService;
    @Bean
    public static PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/register").permitAll()
                        .anyRequest().authenticated()
                ).formLogin(from ->from
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/dashboard",true)
                        .permitAll()
                ).logout(logout -> logout
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // Fixed issue
                .logoutSuccessUrl("/login?logout")
                .permitAll()
                )
                .headers(headers -> headers
                        .frameOptions(frameOption ->frameOption.sameOrigin())
                );
        return http.build();

    }
    @Autowired
    public void  configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(accountService).passwordEncoder(passwordEncoder());
    }


}
