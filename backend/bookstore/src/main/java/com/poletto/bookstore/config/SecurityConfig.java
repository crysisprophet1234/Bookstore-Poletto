package com.poletto.bookstore.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private Environment env;

	@Autowired
	private JWTAuthenticationFilter jwtAuthFilter;

	@Autowired
	private AuthenticationProvider authenticationProvider;
	
	private static final String[] AUTH_WHITELIST = {
			
			//default resources from spring, swagger, h2 and etc
			"/h2-console/**",
	        "/swagger-resources",
	        "/swagger-resources/**",
	        "/configuration/ui",
	        "/configuration/security",
	        "/swagger-ui.html",
	        "/webjars/**",
	        "/v3/api-docs/**",
	        "/auth/**",
	        "/actuator/**",
	        "/swagger-ui/**",
	        
	        //authentication
	        "/auth/**"
	        
	};
	
	private static final String[] ENTITIES_ALL_WHITELIST = {
			
			"books/**",
			"categories/**",
			"authors/**"
						
	};
	
	//TODO adicionar entities que requerem role de operador, admin e etc...

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers(headers -> headers.frameOptions(frame -> frame.disable()));
		}

		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth					
						.requestMatchers(AUTH_WHITELIST).permitAll()
						.requestMatchers(HttpMethod.GET, ENTITIES_ALL_WHITELIST).permitAll()
//						.requestMatchers(HttpMethod.GET, "/api/books/**").authenticated()
//						.requestMatchers(HttpMethod.POST, "/api/reservations/v1/**").hasAuthority("ROLE_CUSTOMER")
//						.requestMatchers(HttpMethod.GET, "/api/categories/v1/**").permitAll()
						.anyRequest().authenticated())
				.requiresChannel(channel -> channel.anyRequest().requiresSecure())
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.cors(cors -> cors.configurationSource(corsConfigurationSource()));

		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOriginPatterns(Arrays.asList("http://localhost:[3000]", "http://localhost:[88]"));
		corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH"));
		corsConfig.setAllowCredentials(true);
		corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		return source;

	}

}
