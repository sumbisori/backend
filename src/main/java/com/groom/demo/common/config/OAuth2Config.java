package com.groom.demo.common.config;

import com.groom.demo.common.oauth2.CustomAuthorizationCodeTokenResponseClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OAuth2Config {

    private final RestTemplate oAuthRestTemplate;

    public OAuth2Config(RestTemplate oAuthRestTemplate) {
        this.oAuthRestTemplate = oAuthRestTemplate;
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> oAuth2AccessTokenResponseClient() {
        return new CustomAuthorizationCodeTokenResponseClient(oAuthRestTemplate);
    }
}
