package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.repository.CardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
    private static final Logger log = LoggerFactory.getLogger(CardService.class);

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card save(Card card) {
        Card saved = cardRepository.save(card);
        log.info("Saved card {}", card.getNumber());
        return saved;
    }

    public List<Card> findAll() {
        List<Card> list = cardRepository.findAll();
        log.info("Found {} cards", list.size());
        return list;
    }

    public Page<Card> findByOwner(String username, Pageable pageable) {
        Page<Card> page = cardRepository.findByOwnerUsername(username, pageable);
        log.info("Found {} cards for user {}", page.getTotalElements(), username);
        return page;
    }

    public Optional<Card> findByIdAndOwner(Long id, String username) {
        Optional<Card> card = cardRepository.findByIdAndOwnerUsername(id, username);
        log.info("Find card {} for user {} -> {}", id, username, card.isPresent());
        return card;
    }

    public Optional<Card> findById(Long id) {
        Optional<Card> card = cardRepository.findById(id);
        log.info("Find card by id {} -> {}", id, card.isPresent());
        return card;
    }

    public Optional<Card> findByNumberAndOwner(String number, String username) {
        Optional<Card> card = cardRepository.findByNumberAndOwnerUsername(number, username);
        log.info("Find card by number {} for user {} -> {}", number, username, card.isPresent());
        return card;
    }

    public Card blockCard(Card card) {
        card.setStatus(CardStatus.BLOCKED);
        Card saved = cardRepository.save(card);
        log.info("Blocked card {}", card.getId());
        return saved;
    }

    public Card activateCard(Card card) {
        card.setStatus(CardStatus.ACTIVE);
        Card saved = cardRepository.save(card);
        log.info("Activated card {}", card.getId());
        return saved;
    }

    public Card deposit(Card card, BigDecimal amount) {
        card.setBalance(card.getBalance().add(amount));
        Card saved = cardRepository.save(card);
        log.info("Deposited {} to card {}", amount, card.getId());
        return saved;
    }

    public Card withdraw(Card card, BigDecimal amount) {
        card.setBalance(card.getBalance().subtract(amount));
        Card saved = cardRepository.save(card);
        log.info("Withdrew {} from card {}", amount, card.getId());
        return saved;
    }

    public void transfer(Card from, Card to, BigDecimal amount) {
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        cardRepository.save(from);
        cardRepository.save(to);
        log.info("Transferred {} from card {} to card {}", amount, from.getId(), to.getId());
    }

    public void delete(Long id) {
        cardRepository.deleteById(id);
        log.info("Deleted card {}", id);
    }
}
