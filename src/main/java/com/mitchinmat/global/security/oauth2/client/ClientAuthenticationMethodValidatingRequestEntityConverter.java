package com.mitchinmat.global.security.oauth2.client;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.AbstractOAuth2AuthorizationGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.util.Assert;

class ClientAuthenticationMethodValidatingRequestEntityConverter<T extends AbstractOAuth2AuthorizationGrantRequest>
	implements Converter<T, RequestEntity<?>> {

	private final Converter<T, RequestEntity<?>> delegate;

	ClientAuthenticationMethodValidatingRequestEntityConverter(Converter<T, RequestEntity<?>> delegate) {
		this.delegate = delegate;
	}

	@Override
	public RequestEntity<?> convert(T grantRequest) {
		ClientRegistration clientRegistration = grantRequest.getClientRegistration();
		ClientAuthenticationMethod clientAuthenticationMethod = clientRegistration.getClientAuthenticationMethod();
		String registrationId = clientRegistration.getRegistrationId();
		boolean supportedClientAuthenticationMethod = clientAuthenticationMethod.equals(ClientAuthenticationMethod.NONE)
			|| clientAuthenticationMethod.equals(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
			|| clientAuthenticationMethod.equals(ClientAuthenticationMethod.CLIENT_SECRET_POST);
		Assert.isTrue(supportedClientAuthenticationMethod, () -> String.format(
			"This class supports `client_secret_basic`, `client_secret_post`, and `none` by default. Client [%s] is using [%s] instead. Please use a supported client authentication method, or use `setRequestEntityConverter` to supply an instance that supports [%s].",
			registrationId, clientAuthenticationMethod, clientAuthenticationMethod));
		return this.delegate.convert(grantRequest);
	}

}
