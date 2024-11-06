package com.groom.demo.domain.user.service;

import com.groom.demo.domain.user.dto.CustomUserDetails;
import com.groom.demo.domain.user.dto.KaKaoResponse;
import com.groom.demo.domain.user.dto.OAuth2Response;
import com.groom.demo.domain.user.entity.User;
import com.groom.demo.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final RestTemplate oAuthRestTemplate;

    private DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @PostConstruct
    public void init() {
        delegate.setRestOperations(oAuthRestTemplate);
    }

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        if (registrationId == null) {
            throw new OAuth2AuthenticationException("유효하지않는 OAuth2 제공자입니다.");
        }
        OAuth2Response oAuth2Response = new KaKaoResponse(oAuth2User.getAttributes());

        User user = findOrCreateMember(oAuth2Response, registrationId);
        return new CustomUserDetails(user, oAuth2User.getAttributes());
    }

    private User findOrCreateMember(OAuth2Response oAuth2Response, String providerType) {
        return userRepository.findByProviderTypeAndProviderId(providerType, oAuth2Response.getProviderId())
                .orElseGet(() -> registerNewMember(oAuth2Response));
    }

    private User registerNewMember(OAuth2Response oAuth2Response) {
        log.info("소셜로그인으로 처음 로그인(강제 회원가입): {}", oAuth2Response.getProvider());
        User user = oAuth2Response.toEntity();
        return userRepository.save(user);
    }
}
