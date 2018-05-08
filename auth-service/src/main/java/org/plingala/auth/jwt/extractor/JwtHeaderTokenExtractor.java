package org.plingala.auth.jwt.extractor;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

@Component
public class JwtHeaderTokenExtractor implements TokenExtractor {

	public static String HEADER_PREFIX = "Bearer ";

    @Override
    public String extract(String header) {
    	if (header == null || header.trim().length() == 0) {
            throw new AuthenticationServiceException("Authorization header cannot be blank!");
        }

        if ( header.startsWith(HEADER_PREFIX) && header.length() < HEADER_PREFIX.length()) {
            throw new AuthenticationServiceException("Invalid authorization header size.");
        }

        return (header.startsWith(HEADER_PREFIX)) ? header.substring(HEADER_PREFIX.length(), header.length()) : header;
    }

}
