package com.groom.demo.domain.user.dto;

import lombok.Builder;

@Builder
public record UserProfile(String nickname, String profileImageUrl, int count) {
}
