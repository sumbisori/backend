package com.groom.sumbisori.domain.place.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @Column
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String address;

    @Column
    private int price;

    @Column(length = 1000)
    private String imageUrl;

    @Column
    private double latitude;

    @Column
    private double longitude;

    @Column
    private String phoneNumber;

    @Column
    private String link;
}
