package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
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

import java.math.BigDecimal;

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

    @Test
    void blockCardSetsStatusAndPersists() {
        Card card = new Card();
        when(cardRepository.save(card)).thenReturn(card);

        Card result = cardService.blockCard(card);

        assertEquals(CardStatus.BLOCKED, card.getStatus());
        verify(cardRepository).save(card);
        assertEquals(card, result);
    }

    @Test
    void depositIncreasesBalanceAndPersists() {
        Card card = new Card();
        card.setBalance(BigDecimal.valueOf(10));
        when(cardRepository.save(card)).thenReturn(card);

        Card result = cardService.deposit(card, BigDecimal.valueOf(5));

        assertEquals(BigDecimal.valueOf(15), card.getBalance());
        verify(cardRepository).save(card);
        assertEquals(card, result);
    }

    @Test
    void withdrawDecreasesBalanceAndPersists() {
        Card card = new Card();
        card.setBalance(BigDecimal.valueOf(10));
        when(cardRepository.save(card)).thenReturn(card);

        Card result = cardService.withdraw(card, BigDecimal.valueOf(3));

        assertEquals(BigDecimal.valueOf(7), card.getBalance());
        verify(cardRepository).save(card);
        assertEquals(card, result);
    }

    @Test
    void transferMovesMoneyBetweenCards() {
        Card from = new Card();
        from.setBalance(BigDecimal.valueOf(20));
        Card to = new Card();
        to.setBalance(BigDecimal.valueOf(5));

        cardService.transfer(from, to, BigDecimal.valueOf(10));

        assertEquals(BigDecimal.valueOf(10), from.getBalance());
        assertEquals(BigDecimal.valueOf(15), to.getBalance());
        verify(cardRepository).save(from);
        verify(cardRepository).save(to);
    }
}
