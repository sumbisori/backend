package com.groom.demo.domain.place.controller;

import static com.groom.demo.domain.place.error.PlaceErrorcode.Const.PLACE_NOT_FOUND;

import com.groom.demo.common.springdoc.ApiExceptionExplanation;
import com.groom.demo.common.springdoc.ApiResponseExplanations;
import com.groom.demo.domain.place.dto.PlaceMapResponse;
import com.groom.demo.domain.place.dto.PlaceResponse;
import com.groom.demo.domain.place.error.PlaceErrorcode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "places", description = "해녀체험 장소 API")
public interface PlaceApi {
    @Operation(summary = "해녀체험 장소 목록 조회")
    public ResponseEntity<List<PlaceMapResponse>> getAllPlaces();

    @Operation(summary = "해녀체험 장소 상세 조회")
    @ApiResponseExplanations(
            errors = {
                    @ApiExceptionExplanation(value = PlaceErrorcode.class, constant = PLACE_NOT_FOUND, name = "해당 장소가 존재하지 않을 때"),
            }
    )
    public ResponseEntity<PlaceResponse> getPlaceInfo(@PathVariable Long placeId);
}
