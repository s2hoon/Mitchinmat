package com.mitchinmat.global.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.mitchinmat.global.security.jwt.filter.JwtAuthenticationFilter;
import com.mitchinmat.global.security.jwt.filter.JwtExceptionHandlerFilter;
import com.mitchinmat.global.security.jwt.handler.JwtAccessDeniedHandler;
import com.mitchinmat.global.security.jwt.handler.JwtAuthenticationEntryPoint;
import com.mitchinmat.global.security.oauth2.client.CustomAuthorizationCodeTokenResponseClient;
import com.mitchinmat.global.security.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import com.mitchinmat.global.security.oauth2.handler.Oauth2AuthenticationFailureHandler;
import com.mitchinmat.global.security.oauth2.handler.Oauth2AuthenticationSuccessHandler;
import com.mitchinmat.global.security.oauth2.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private static final List<String> SWAGGER = List.of("/swagger-ui.html", "/swagger-ui/**", "/swagger-resources/**",
		"/v3/api-docs/**");
	private static final List<String> AUTH = List.of("/oauth2/**", "/oauth/callback/**", "/api/v2/token/**");

	private final CustomOAuth2UserService oauth2UserService;
	private final Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;
	private final Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;
	private final HttpCookieOAuth2AuthorizationRequestRepository
		httpCookieOAuth2AuthorizationRequestRepository;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.oauth2Login(oauth2 -> oauth2
					.authorizationEndpoint(authorization -> authorization
						.baseUri("/oauth2/authorization")
						.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
					)
					.redirectionEndpoint(redirection -> redirection
						.baseUri("/oauth/callback/*"))
					.userInfoEndpoint(userInfo -> userInfo
						.userService(oauth2UserService))
					.successHandler(oauth2AuthenticationSuccessHandler)
					.failureHandler(oauth2AuthenticationFailureHandler)
				// Custom Token Response Client 사용
			);

		http
			.authorizeRequests(authorize -> authorize
				.requestMatchers(SWAGGER.toArray(String[]::new))
				.permitAll()
				.requestMatchers(AUTH.toArray(String[]::new))
				.permitAll()
				.requestMatchers("/actuator/**")
				.permitAll()
				.requestMatchers(HttpMethod.GET, "/api/place/{placeId}", "/api/good-place/{placeId}")
				.hasAnyRole("USER", "ANONYMOUS")
				.requestMatchers("/api/good-place/search/**")
				.hasAnyRole("USER", "ANONYMOUS")
				.anyRequest()
				.hasRole("USER")
			);

		http
			.exceptionHandling((exceptions) -> exceptions
				.authenticationEntryPoint(new JwtAuthenticationEntryPoint()) // 인증 실패 핸들링
				.accessDeniedHandler(new JwtAccessDeniedHandler())); // 인가 실패 핸들링

		http
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new JwtExceptionHandlerFilter(), JwtAuthenticationFilter.class);

		return http.build();

	}

	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("https://localhost:3000", "http://localhost:3000",
			"http://app.mitchinmat.com", "https://app.mitchinmat.com",
			"http://mitchinmat.com", "https://mitchinmat.com"));
		config.setAllowedMethods(List.of("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS"));
		config.setAllowedHeaders(List.of("Authorization", "Set-Cookie", "Content-Type"));
		config.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
		config.setAllowCredentials(true);
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	static RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
		hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER > ROLE_ANONYMOUS");
		return hierarchy;
	}

	@Bean
	static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		expressionHandler.setRoleHierarchy(roleHierarchy);
		return expressionHandler;
	}

	@Bean
	public CustomAuthorizationCodeTokenResponseClient customAuthorizationCodeTokenResponseClient() {
		return new CustomAuthorizationCodeTokenResponseClient();  // Custom Token Response Client Bean 등록
	}
}
