package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.repository.CardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с карточками в базе данных.
 */
@Service
public class CardService {
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card save(Card card) {
        return cardRepository.save(card);
    }

    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    public Page<Card> findByOwner(String username, Pageable pageable) {
        return cardRepository.findByOwnerUsername(username, pageable);
    }

    public Optional<Card> findByIdAndOwner(Long id, String username) {
        return cardRepository.findByIdAndOwnerUsername(id, username);
    }

    public Optional<Card> findById(Long id) {
        return cardRepository.findById(id);
    }

    public Optional<Card> findByNumberAndOwner(String number, String username) {
        return cardRepository.findByNumberAndOwnerUsername(number, username);
    }

    public Card blockCard(Card card) {
        card.setStatus(CardStatus.BLOCKED);
        return cardRepository.save(card);
    }

    public Card activateCard(Card card) {
        card.setStatus(CardStatus.ACTIVE);
        return cardRepository.save(card);
    }

    public Card deposit(Card card, BigDecimal amount) {
        card.setBalance(card.getBalance().add(amount));
        return cardRepository.save(card);
    }

    public Card withdraw(Card card, BigDecimal amount) {
        card.setBalance(card.getBalance().subtract(amount));
        return cardRepository.save(card);
    }

    public void transfer(Card from, Card to, BigDecimal amount) {
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        cardRepository.save(from);
        cardRepository.save(to);
    }

    public void delete(Long id) {
        cardRepository.deleteById(id);
    }
}
