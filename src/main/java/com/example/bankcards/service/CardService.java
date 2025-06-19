package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business service for working with cards in the database.
 */
@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;

    public Card save(Card card) {
        return cardRepository.save(card);
    }

    public List<Card> findAll() {
        return cardRepository.findAll();
    }
}
