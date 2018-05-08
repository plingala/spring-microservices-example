package org.plingala.auth.ajax;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.plingala.auth.common.WebUtil;
import org.plingala.auth.exceptions.AuthMethodNotSupportedException;

public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
	
	private final AuthenticationSuccessHandler successHandler;
	private final AuthenticationFailureHandler failureHandler;
	
	private final ObjectMapper objectMapper;
	
	public AjaxLoginProcessingFilter(String defaultFilterProcessesUrl, AuthenticationSuccessHandler successHandler,
			AuthenticationFailureHandler failureHandler, ObjectMapper objectMapper) {
		super(defaultFilterProcessesUrl);
		this.successHandler = successHandler;
		this.failureHandler = failureHandler;
		this.objectMapper = objectMapper;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		
//		boolean flag = WebUtil.isContentTypeJson(request);
//		boolean f1 = HttpMethod.POST.name().equals(request.getMethod());
//		boolean f2 = WebUtil.isAjax(request);
		
		if(!HttpMethod.POST.name().equals(request.getMethod()) || !WebUtil.isContentTypeJson(request)) {
			if(logger.isDebugEnabled()) {
				logger.debug("Authentication method not supported. Request method: " + request.getMethod());
			}
			
			throw new AuthMethodNotSupportedException("Authenticationmethod not supoprted");
		}
		
		LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
		
		if( (loginRequest.getUsername() == null || loginRequest.getUsername().trim().length() == 0) || (loginRequest.getPassword() == null || loginRequest.getPassword().trim().length() == 0) ) {
			throw new AuthenticationServiceException("Username or password not provided");
		}
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
		
		return this.getAuthenticationManager().authenticate(token);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		successHandler.onAuthenticationSuccess(request, response, authResult);
		
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		
		SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
	
	}
}