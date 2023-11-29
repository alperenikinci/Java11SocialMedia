package com.bilgeadam.config.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthServiceSecurityConfig {

    @Bean
    JwtTokenFilter getJwtTokenFilter(){
        return new JwtTokenFilter();
    }
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable(); //cross-site-request-forgery
        httpSecurity.authorizeRequests().requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/api/v1/auth/login",
                        "/api/v1/auth/register",
                        "/api/v1/auth/register2",
                        "/api/v1/auth/activate_status")
                .permitAll().anyRequest().authenticated();
//        httpSecurity.authorizeRequests().requestMatchers("/v3/api-docs/**","/swagger-ui/**").authenticated().anyRequest().permitAll();
//         antMatchers -> requestMatchers
//        httpSecurity.formLogin();

        httpSecurity.addFilterBefore(getJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}
