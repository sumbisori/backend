package com.groom.demo.domain.reservation.dto;

import com.groom.demo.domain.reservation.Status;
import lombok.Builder;

@Builder
public record ReservationDto(String reservationDate, String reservationTime, Status status, String personName,
                             String imageUrl, String name, String address, int peopleCount) {
}
