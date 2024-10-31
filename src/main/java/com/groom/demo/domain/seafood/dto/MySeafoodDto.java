package com.groom.demo.domain.seafood.dto;

import com.groom.demo.domain.seafood.Seafood;
import java.time.LocalDateTime;

public record MySeafoodDto(Seafood seafood, String name, String englishName, String description,
                           LocalDateTime insDt, int count) {

}
