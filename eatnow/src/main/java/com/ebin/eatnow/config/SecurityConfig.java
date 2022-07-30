package com.ebin.eatnow.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ebin.eatnow.utils.JwtFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtFilter jwtFilter;

    // @Bean
	// @Override
	// public AuthenticationManager authenticationManagerBean() throws Exception {
	// 	return super.authenticationManagerBean();
	// }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http = http.cors().and().csrf().disable();

        http = http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and();

        http = http
            .exceptionHandling()
            .authenticationEntryPoint(
                (request, response, ex) -> {
                    response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        ex.getMessage()
                    );
                }
            )
            .and();

        http.authorizeRequests()
            .antMatchers("/users/login").permitAll()
            .antMatchers(HttpMethod.GET, "/restaurant/**").permitAll()
            .antMatchers(HttpMethod.GET, "/restaurant/{restaurantId}").permitAll()
            .antMatchers(HttpMethod.GET, "/menu/**").permitAll()
            .anyRequest().authenticated();

        http.addFilterBefore(
            jwtFilter,
            UsernamePasswordAuthenticationFilter.class
        );
    }
}
