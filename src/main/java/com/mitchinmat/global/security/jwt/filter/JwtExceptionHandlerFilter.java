package com.mitchinmat.global.security.jwt.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchinmat.global.common.response.ResponseUtils;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {
	/**
	 * 토큰 관련 에러 핸들링
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (JwtException e) {
			setErrorResponse(request, response, e);
		}
	}

	private void setErrorResponse(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");

		ObjectMapper objectMapper = new ObjectMapper();

		Map<String, Object> data = new HashMap<>();
		Map<String, Object> error = new HashMap<>();
		error.put("status", HttpServletResponse.SC_UNAUTHORIZED);
		error.put("message", ex.getMessage());
		error.put("path", request.getServletPath());
		data.put("error", error);

		try {
			response.getWriter().write(objectMapper.writeValueAsString(
				ResponseUtils.error(null, data, "Unauthorized")
			));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
