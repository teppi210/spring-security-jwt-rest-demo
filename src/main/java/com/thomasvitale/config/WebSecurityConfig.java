package com.thomasvitale.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.thomasvitale.security.JWTAuthenticationFilter;
import com.thomasvitale.security.JWTLoginFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			// Disable CSRF protection since tokens are immune to it
			.csrf().disable()
			// Security policy
			.authorizeRequests()
				// Allow anonymous access to "/" path
				.antMatchers("/").permitAll()
				// Allow anonymous access to "/login" (only POST requests)
				.antMatchers(HttpMethod.POST, "/login").permitAll()
				// Any other request must be authenticated
				.anyRequest().authenticated().and()
			// Custom filter for logging in users at "/login"
			.addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
			// Custom filter for authenticating users using tokens
			.addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Create a default account
		auth
			.inMemoryAuthentication()
				.withUser("user")
				.password("password")
				.roles("USER");
	}

}