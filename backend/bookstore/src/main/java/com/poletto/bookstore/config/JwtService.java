package com.poletto.bookstore.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.poletto.bookstore.services.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
@PropertySource("classpath:application.properties")
public class JwtService {

	@Value("${jwt.secret}")
	private String SECRET_KEY;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		
		Claims claims = Jwts.claims();
		claims.setSubject(userDetails.getUsername());
		claims.setExpiration(new Date(System.currentTimeMillis() + 86400000));
		claims.setIssuedAt(new Date(System.currentTimeMillis()));

		claims.put("authorities", userDetails.getAuthorities()
												.stream()
												.map(x -> x.getAuthority())
													.collect(Collectors.toList())
													.toArray());
		
		return Jwts.builder()
				.setClaims(claims)
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();	

	}

	public boolean isTokenValid(String token, UserDetails userDetails) {

		final String username = extractUsername(token);

		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);

	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
