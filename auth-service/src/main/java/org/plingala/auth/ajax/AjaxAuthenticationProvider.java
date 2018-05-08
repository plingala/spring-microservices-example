package org.plingala.auth.ajax;

import java.util.List;

import org.plingala.auth.domain.support.MyUserDetails;
import org.plingala.auth.domain.support.UserContext;
import org.plingala.domain.User;
import org.plingala.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.lang.Assert;

@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider {
	private final BCryptPasswordEncoder encoder;
	private UserRepository userRepository;
	
	@Autowired
	public AjaxAuthenticationProvider(BCryptPasswordEncoder encoder, UserRepository userRepository) {
		this.encoder = encoder;
		this.userRepository = userRepository;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.notNull(authentication, "Not authentication provided");
		
		String username = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();
		
		User user = userRepository.findByUserName(username);
		
		UserDetails userDetails = new MyUserDetails(user);
		
		if(user == null) {
			throw new UsernameNotFoundException("Username not found " + username);
		}
		
		if(!password.equals(user.getPassword())){
			throw new BadCredentialsException("Authentication failed. Invalid password");
		}
		
		UserContext userContext = UserContext.create(user.getUsername(), user.getName(), (List<GrantedAuthority>) userDetails.getAuthorities());
		
		return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
