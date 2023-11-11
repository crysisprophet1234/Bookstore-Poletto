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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.poletto.bookstore.exceptions.handler.CustomAccessDeniedHandler;
import com.poletto.bookstore.exceptions.handler.CustomAuthenticationEntryPoint;

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

		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth
					.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/auth/**")).permitAll()
					.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/books/**")).permitAll()
					.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/categories/**")).permitAll()
					.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/authors/**")).permitAll()
					.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/reservations/**")).hasRole("OPERATOR")
					.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/books/**")).hasRole("OPERATOR")
					.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/reservations/**")).hasRole("OPERATOR")
					.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/books/**")).hasRole("OPERATOR")
					.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/reservations/**")).hasRole("OPERATOR")
					.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "/books/**")).hasRole("ADMIN")				
					.requestMatchers(AntPathRequestMatcher.antMatcher("/users/**")).hasRole("ADMIN")
					.anyRequest().permitAll())
			.requiresChannel(channel -> channel.anyRequest().requiresSecure())
			.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authenticationProvider(authenticationProvider)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.exceptionHandling(exHandler -> exHandler
		        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
		        .accessDeniedHandler(new CustomAccessDeniedHandler()));
		
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOriginPatterns(Arrays.asList(
				"http://localhost:[3000]",
				"http://localhost:[88]",
				"https://polettobookstore.netlify.app/"));
		corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH"));
		corsConfig.setAllowCredentials(true);
		corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		return source;

	}

}
