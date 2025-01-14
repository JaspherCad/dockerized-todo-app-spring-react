package com.jaspher.rest.webservices.restfulwebservices.basic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class BasicAuthenticationSecurityConfig {
	//1 Configure the Filter chain BECAUSE IF WE DO THIS, 
		//WE ARE REVERTING THE SECURITY TO DEFAULT which is not having security
	//2 we must Authenticate all request for practice
	//3 basic authentication (the pop up login) built in ito
	//4 disable csrf (required for stateless) 
	//5 must have no session (requeires no csrf) // stateless rest api	
	
	//queston? WHY DO THIS DISABLE CSRF? because spring will not
		//allow us to POST request because of CSRF session. 
		//Since spring security default requires csrf session, we have to customize 
		//the security configuration from the top. 
		//BONUS: by default, everything requires authentication which is bad...
	
	//@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests(
				auth -> 
					auth.antMatchers(HttpMethod.OPTIONS, "/**").permitAll() //for preflight (look at js)
					.anyRequest().permitAll()
				); //this requires all http to be authenticated
		
		http.httpBasic(Customizer.withDefaults());
		
		http.sessionManagement(
				session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				);
		
		http.csrf().disable();
		http.headers().frameOptions().disable();// now that csrf is disabled, i can do POST request.
		//note: even without csrf we can do GET request.
		
		
		return http.build();
	}
	
}
