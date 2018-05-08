package org.plingala.auth.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.plingala.auth.config.JwtSettings;
import org.plingala.auth.domain.support.AccessJwtToken;
import org.plingala.auth.domain.support.JwtToken;
import org.plingala.auth.domain.support.Scopes;
import org.plingala.auth.domain.support.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenFactory {
	
	private final JwtSettings settings;
	private Logger logger = LoggerFactory.getLogger(getClass());

	    @Autowired
	    public JwtTokenFactory(JwtSettings settings) {
	        this.settings = settings;
	    }

	    /**
	     * Factory method for issuing new JWT Tokens.
	     * 
	     * @param username
	     * @param roles
	     * @return
	     */
	    public AccessJwtToken createAccessJwtToken(UserContext userContext) {
	    	logger.debug(userContext.toString());
	    	if (userContext.getUsername() == null || userContext.getUsername().trim().length() == 0) 
	            throw new IllegalArgumentException("Cannot create JWT Token without username");

	        if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty()) 
	            throw new IllegalArgumentException("User doesn't have any privileges");

	        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
	        claims.put("name", userContext.getName());
	        claims.put("scopes", userContext.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));

	        LocalDateTime currentTime = LocalDateTime.now();

	        String token = Jwts.builder()
	          .setClaims(claims)
	          .setIssuer(settings.getTokenIssuer())
	          .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
	          .setExpiration(Date.from(currentTime
	                  .plusMinutes( (settings.getTokenExpirationTime() ==  null) ? 0 : settings.getTokenExpirationTime() )
	                  .atZone(ZoneId.systemDefault()).toInstant()))
	          .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
	        .compact();

	        return new AccessJwtToken(token, claims);
	    }

	    public JwtToken createRefreshToken(UserContext userContext) {
	    	if (userContext.getUsername() == null || userContext.getUsername().trim().length() == 0) {
	            throw new IllegalArgumentException("Cannot create JWT Token without username");
	        }

	        LocalDateTime currentTime = LocalDateTime.now();

	        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
	        claims.put("name", userContext.getName());
	        claims.put("scopes", Arrays.asList(Scopes.REFRESH_TOKEN.authority()));

	        String token = Jwts.builder()
	          .setClaims(claims)
	          .setIssuer(settings.getTokenIssuer())
	          .setId(UUID.randomUUID().toString())
	          .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
	          .setExpiration(Date.from(currentTime
	                  .plusMinutes( (settings.getRefreshTokenExpTime() ==  null) ? 0 : settings.getRefreshTokenExpTime() )
	                  .atZone(ZoneId.systemDefault()).toInstant()))
	          .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
	        .compact();

	        return new AccessJwtToken(token, claims);
	    }
}
