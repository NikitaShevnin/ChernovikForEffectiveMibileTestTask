package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.repository.CardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Transactional
    public Card save(Card card) {
        Card saved = cardRepository.save(card);
        return saved;
    }

    public List<Card> findAll() {
        List<Card> list = cardRepository.findAll();
        return list;
    }

    public Page<Card> findByOwner(String username, Pageable pageable) {
        Page<Card> page = cardRepository.findByOwnerUsername(username, pageable);
        return page;
    }

    public Optional<Card> findByIdAndOwner(Long id, String username) {
        Optional<Card> card = cardRepository.findByIdAndOwnerUsername(id, username);
        return card;
    }

    public Optional<Card> findById(Long id) {
        Optional<Card> card = cardRepository.findById(id);
        return card;
    }

    public Optional<Card> findByNumberAndOwner(String number, String username) {
        Optional<Card> card = cardRepository.findByNumberAndOwnerUsername(number, username);
        return card;
    }

    @Transactional
    public Card blockCard(Card card) {
        card.setStatus(CardStatus.BLOCKED);
        Card saved = cardRepository.save(card);
        return saved;
    }

    @Transactional
    public Card activateCard(Card card) {
        card.setStatus(CardStatus.ACTIVE);
        Card saved = cardRepository.save(card);
        return saved;
    }

    @Transactional
    public Card deposit(Card card, BigDecimal amount) {
        card.setBalance(card.getBalance().add(amount));
        Card saved = cardRepository.save(card);
        return saved;
    }

    @Transactional
    public Card withdraw(Card card, BigDecimal amount) {
        card.setBalance(card.getBalance().subtract(amount));
        Card saved = cardRepository.save(card);
        return saved;
    }

    @Transactional
    public void transfer(Card from, Card to, BigDecimal amount) {
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        cardRepository.save(from);
        cardRepository.save(to);
    }

    @Transactional
    public void delete(Long id) {
        cardRepository.deleteById(id);
    }
}
