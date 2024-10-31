package com.groom.demo.domain.place;

import java.util.List;
import lombok.Getter;

@Getter
public enum Place {
    SA_GYE_ORCHUN_EXPERIENCE_VILLAGE("제주 서귀포 사계어촌체험마을", "안전교육, 환복, 물질 체험, 해산물 시식", "제주 서귀포시 안덕면 형제해안로 13-1 사계어촌계", 50000,
            "https://i.ibb.co/j82zgnG/image.png"
            , List.of("2024.11.04", "2024.11.06", "2024.11.10", "2024.11.15", "2024.11.20")),
    CITY_HAE_NAE("도시해녀", "1일 해녀체험, 장비대여, 수중사진, 샤워용품제공, 해산물시식", "제주특별자치도 제주시 애월읍 하귀미수포길 16-1", 50000,
            "https://i.ibb.co/LPW1BSH/image.jpg"
            , List.of("2024.11.05", "2024.11.07", "2024.11.11", "2024.11.16", "2024.11.21")),
    HYEOB_JAE_HAE_NAE_DABANG_EXPERIENCE("협재해녀다방&해녀체험", "해녀체험, 장비대여, 수중사진, 온수샤워, 해물라면", "제주 제주시 한림읍 협재1길 42 협재해녀다방", 50000,
            "https://i.ibb.co/Rv4Rr0r/image.jpg"
            , List.of("2024.11.05", "2024.11.07", "2024.11.11", "2024.11.16", "2024.11.21")),

    MODE_RACK_BADANG("모드락바당", "스노클링, 수중플로깅, 해녀 선생님과 물질체험", "제주 서귀포시 성산읍 일출로 264-4", 69000,
            "https://i.ibb.co/WGj6xxg/image.jpg"
            , List.of("2024.11.05", "2024.11.09", "2024.11.13", "2024.11.18", "2024.11.23")),

    JEJU_HAE_NAE_EXPERIENCE("제주 해녀체험", "장비대여, 샤워시설 이용, 해산물시식, 예약 필수", "제주 서귀포시 남원읍 하례망장포로 65-13", 50000,
            "https://i.ibb.co/6JSh8HD/image.jpg"
            , List.of("2024.11.06", "2024.11.10", "2024.11.14", "2024.11.19", "2024.11.24"));

    private final String name;
    private final String description;
    private final String address;
    private final int price;
    private final String imageUrl;
    private final List<String> availableDate;

    Place(String name, String description, String address, int price, String imageUrl, List<String> availableDate) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.price = price;
        this.imageUrl = imageUrl;
        this.availableDate = availableDate;
    }
}
