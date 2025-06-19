package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveDelegatesToRepository() {
        Card card = new Card();
        when(cardRepository.save(card)).thenReturn(card);

        Card saved = cardService.save(card);

        verify(cardRepository).save(card);
        assertEquals(card, saved);
    }

    @Test
    void findAllReturnsListFromRepository() {
        List<Card> cards = Arrays.asList(new Card(), new Card());
        when(cardRepository.findAll()).thenReturn(cards);

        List<Card> result = cardService.findAll();

        verify(cardRepository).findAll();
        assertEquals(cards, result);
    }
}
