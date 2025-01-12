package com.groom.sumbisori.domain.content.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Spot {
    JEJU_HARBOR(22457L, "제주항", 33.5248, 126.4943),
    CHUJADO(22468L, "추자도", 33.9723, 126.2785),
    GAPADO(22476L, "가파도", 33.1625, 126.2630),
    UDO(22469L, "우도", 33.5122, 126.6338),
    JUNGMOON(22458L, "중문", 33.2250, 126.3918),
    YEONGNAK(22505L, "영락", 33.2392, 126.1937),
    SHINSAN(22495L, "신산", 33.3778, 126.9063),
    GUEOM(22514L, "구엄", 33.5205, 126.3747),
    WIMI(22515L, "위미", 33.2233, 126.7098);

    private Long spotId;
    private String spotName;
    private double latitude;
    private double longitude;
}
