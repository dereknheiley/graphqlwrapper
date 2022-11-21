package com.example.takehome;

import static com.example.takehome.controller.ContinentController.CONTINENTS;
import static com.example.takehome.controller.ContinentController.CONTINENTS_COUNTRIES;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Deprecated
	/** TODO demo credentials only, don't actually use this in production */
	public static final String USERPASS = "asdf";

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers("/actuator", "/actuator/health").permitAll()
			.antMatchers(CONTINENTS).permitAll()
			.anyRequest().authenticated()
			.and().httpBasic() //mostly for testing
			.and()
				.formLogin()
				// redirect back to our REST api after login
				.defaultSuccessUrl(CONTINENTS_COUNTRIES, true)
				.permitAll()
			;
	}

	// TODO add user registration with offline credential storage in DB
	@Deprecated
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
			.withUser(USERPASS)
			.password(passwordEncoder().encode(USERPASS))
			.roles("USER");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
