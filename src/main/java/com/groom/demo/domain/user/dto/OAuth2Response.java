package com.groom.demo.domain.user.dto;

import com.groom.demo.domain.user.entity.User;
import com.groom.demo.domain.user.entity.User.UserRole;

public interface OAuth2Response {
    String getProvider();

    String getProviderId();

    String getEmail();

    String getNickname();

    String getProfileImage();

    default User toEntity() {
        return User.builder()
                .email(getEmail())
                .providerType(getProvider())
                .providerId(getProviderId())
                .nickname(getNickname())
                .profileImageUrl(getProfileImage())
                .userRole(UserRole.USER)
                .build();
    }
}
