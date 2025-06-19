package com.example.bankcards.dto;

import com.example.bankcards.entity.Card;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardDto {
    private Long id;
    private String number;
    private String expiry;
    private String status;
    private BigDecimal balance;

    public static CardDto fromEntity(Card card) {
        CardDto dto = new CardDto();
        dto.setId(card.getId());
        dto.setNumber(card.getNumber());
        dto.setExpiry(card.getExpiry());
        dto.setStatus(card.getStatus().name());
        dto.setBalance(card.getBalance());
        return dto;
    }
}
