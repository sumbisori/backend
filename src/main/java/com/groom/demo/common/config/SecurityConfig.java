package com.groom.demo.common.config;

import com.groom.demo.common.filter.HttpCookieOAuth2AuthorizationRequestRepository;
import com.groom.demo.common.filter.JWTFilter;
import com.groom.demo.common.util.JWTUtil;
import com.groom.demo.domain.user.oauth2.CustomFailureHandler;
import com.groom.demo.domain.user.oauth2.CustomSuccessHandler;
import com.groom.demo.domain.user.service.CustomOAuth2UserService;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomFailureHandler customFailureHandler;
    private final JWTUtil jwtUtil;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private RestTemplate proxiedRestTemplate;

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        // 프록시가 적용된 AccessTokenResponseClient 생성
        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient =
                new DefaultAuthorizationCodeTokenResponseClient();
        accessTokenResponseClient.setRestOperations(proxiedRestTemplate);

        return http.authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable).disable())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
//                .exceptionHandling((auth) -> auth.authenticationEntryPoint(customAuthenticationEntryPoint)
//                        .accessDeniedHandler(customAccessDeniedHandler))
                .oauth2Login(oauth2 -> oauth2
                        .tokenEndpoint(tokenEndpointConfig -> tokenEndpointConfig
                                .accessTokenResponseClient(accessTokenResponseClient)
                        )
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)
                        )
                        .authorizationEndpoint(authorization -> authorization
                                .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
                        )
                        .successHandler(customSuccessHandler)
                        .failureHandler(customFailureHandler)
                )
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));  // 모든 출처 허용
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setExposedHeaders(Collections.singletonList("Authorization"));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
