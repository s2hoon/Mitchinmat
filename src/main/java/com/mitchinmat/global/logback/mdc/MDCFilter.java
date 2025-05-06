package com.mitchinmat.global.logback.mdc;

import java.io.IOException;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import com.mitchinmat.global.util.HttpRequestUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MDCFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		HttpServletRequest httpReq = WebUtils.getNativeRequest(request, HttpServletRequest.class);

		MDCUtil.setJsonValue(MDCUtil.REQUEST_URI_MDC, HttpRequestUtil.getRequestUri(Objects.requireNonNull(httpReq)));
		MDCUtil.setJsonValue(MDCUtil.USER_IP_MDC, HttpRequestUtil.getClientIP(Objects.requireNonNull(httpReq)));
		MDCUtil.setJsonValue(MDCUtil.HEADER_MAP_MDC, HttpRequestUtil.getHeaderMap(httpReq));
		MDCUtil.setJsonValue(MDCUtil.PARAMETER_MAP_MDC, HttpRequestUtil.getParamMap(httpReq));
		MDCUtil.setJsonValue(MDCUtil.USER_AGENT_DETAIL_MDC, HttpRequestUtil.getAgentDetail(httpReq));

		filterChain.doFilter(request, response);
	}
}
