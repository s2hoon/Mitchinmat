package com.mitchinmat.global.security.jwt;

import static com.mitchinmat.global.error.ErrorCode.*;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;
import com.mitchinmat.global.security.token.domain.AuthToken;
import com.mitchinmat.global.security.token.domain.AuthTokenType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_TYPE = "Bearer ";
	private final Long ACCESS_TOKEN_VALID_MILLISECOND;
	private final Long REFRESH_TOKEN_VALID_MILLI_SECOND;
	private final String SECRET_KEY;

	@Autowired
	public JwtTokenProvider(
		@Value("${auth.token.secret-key}") String secretKey,
		@Value("${auth.token.access-token-valid-millisecond}") Long accessTokenValidMillisecond,
		@Value("${auth.token.refresh-token-valid-millisecond}") Long refreshTokenValidMillisecond) {
		this.SECRET_KEY = Base64.getEncoder().encodeToString(secretKey.getBytes());
		this.ACCESS_TOKEN_VALID_MILLISECOND = accessTokenValidMillisecond;
		this.REFRESH_TOKEN_VALID_MILLI_SECOND = refreshTokenValidMillisecond;
	}

	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
			return bearerToken.substring(7);
		}
		return bearerToken;
	}

	public AuthToken createAccessToken(Authentication authentication) {
		AuthToken token = createToken(authentication, ACCESS_TOKEN_VALID_MILLISECOND, AuthTokenType.ACCESS);
		return token;
	}

	public AuthToken createRefreshToken(Authentication authentication) {
		AuthToken token = createToken(authentication, REFRESH_TOKEN_VALID_MILLI_SECOND, AuthTokenType.REFRESH);
		return token;
	}

	private AuthToken createToken(Authentication authentication, Long expiration, AuthTokenType authTokenType) {
		CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();

		List<String> authorities = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.toList());

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expiration);
		final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));

		String token = Jwts.builder()
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.setSubject("AuthToken")
			.claim("userId", customOAuth2User.getUserId())
			.claim("oauth2Id", customOAuth2User.getOauth2Id())
			.claim("authorities", authorities)
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();

		return AuthToken.builder()
			.userId(customOAuth2User.getUserId())
			.token(token)
			.expiresIn(expiration)
			.type(authTokenType)
			.build();
	}

	public Authentication getAuthentication(String token) throws JwtException {
		Claims claims = parseClaims(token);

		Long userId = claims.get("userId", Long.class);
		String oauth2Id = claims.get("oauth2Id", String.class);
		List<?> rawAuthorities = claims.get("authorities", List.class);

		List<SimpleGrantedAuthority> authorities =
			rawAuthorities.stream()
				.filter(authority -> authority instanceof String)
				.map(authority -> new SimpleGrantedAuthority((String)authority))
				.toList();

		CustomOAuth2User principal = CustomOAuth2User.of(userId, oauth2Id, authorities);

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	private Claims parseClaims(String token) throws JwtException {
		return Jwts
			.parserBuilder()
			.setSigningKey(SECRET_KEY)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			throw new JwtException(TOKEN_INVALID.getMessage());
		} catch (ExpiredJwtException e) {
			throw new JwtException(TOKEN_EXPIRED.getMessage());
		} catch (UnsupportedJwtException e) {
			throw new JwtException(TOKEN_UNSUPPORTED.getMessage());
		} catch (IllegalArgumentException e) {
			throw new JwtException(TOKEN_WRONG.getMessage());
		}
	}

}
