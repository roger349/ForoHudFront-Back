package com.rer.ForoHubBackEndApp.Security;

import com.rer.ForoHubBackEndApp.Services.UserDetailsServiceUsuario;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    UserDetailsServiceUsuario userDetailsServiceUsuario;
    @Lazy
    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.requireCsrfProtectionMatcher(request -> false))
                .sessionManagement(se -> se.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/login", "/registrarUsuario", "/index").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/v3/api-docs", "/swagger-ui/**", "/swagger-ui/index.html").permitAll()
                        .anyRequest().authenticated())
                        .formLogin().loginPage("/login.html")
                        .defaultSuccessUrl("/index.html", true)
                        .permitAll().and().logout().permitAll();;
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
       return http.getSharedObject(AuthenticationManagerBuilder.class)
               .userDetailsService(userDetailsServiceUsuario)
               .passwordEncoder(passwordEncoder).and()
               .build();
   }
   /*@Bean
   public void Configuration(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .formLogin().loginPage("/login.html")
                .defaultSuccessUrl("/home.html", true)
                .permitAll().and().logout().permitAll();
    }*/
}







