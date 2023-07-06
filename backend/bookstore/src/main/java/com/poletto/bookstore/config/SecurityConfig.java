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

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers(headers -> headers.frameOptions(frame -> frame.disable()));
		}

		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/api/auth/v1/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/reservations/v1/**").hasAuthority("ROLE_CUSTOMER")
						.requestMatchers(HttpMethod.GET, "/api/categories/v1/**").permitAll()
						// .anyRequest().hasAuthority("ROLE_ADMIN")
						.anyRequest().permitAll())
				.requiresChannel(channel -> //https
						channel.anyRequest().requiresSecure())
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.cors(cors -> cors.configurationSource(corsConfigurationSource()));

		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOriginPatterns(Arrays.asList("http://localhost:[3000]"));
		corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH"));
		corsConfig.setAllowCredentials(true);
		corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		return source;

	}

}
