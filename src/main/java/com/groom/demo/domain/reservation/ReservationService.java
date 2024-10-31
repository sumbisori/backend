package com.groom.demo.domain.reservation;

import com.groom.demo.domain.place.Place;
import com.groom.demo.domain.reservation.dto.ReservationRequest;
import com.groom.demo.domain.user.entity.User;
import com.groom.demo.domain.user.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public List<Place> findPlace() {
        return Arrays.stream(Place.values())
                .collect(Collectors.toList());
    }

    public void createReservation(Long userId, ReservationRequest reservationRequest) {
        // 예약 생성 로직을 추가합니다.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Reservation reservation = Reservation.builder()
                .reservationDate(reservationRequest.getSelectedAvailableDate())
                .reservationTime(reservationRequest.getSelectedTime())
                .isCompleted(false)
                .name(reservationRequest.getPersonName())
                .place(reservationRequest.getPlace())
                .numberOfPeople(reservationRequest.getPeopleCount())
                .id(user.getId())
                .build();
        reservationRepository.save(reservation);

    }
}
