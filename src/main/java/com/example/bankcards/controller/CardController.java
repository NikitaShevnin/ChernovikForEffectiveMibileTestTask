package com.example.bankcards.controller;

import com.example.bankcards.dto.AmountDto;
import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.TransferDto;
import com.example.bankcards.dto.CreateCardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


/**
 * Контроллер для работы с API, связанным с картами.
 */
@RestController
@RequestMapping("/api/cards")
public class CardController {
    private final CardService cardService;
    private final UserService userService;

    public CardController(CardService cardService, UserService userService) {
        this.cardService = cardService;
        this.userService = userService;
    }

    @GetMapping
    public java.util.List<CardDto> all() {
        java.util.List<CardDto> list = cardService.findAll().stream()
                .map(CardDto::fromEntity)
                .toList();
        return list;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CardDto create(@Valid @RequestBody CreateCardDto dto) {
        Card card = new Card();
        card.setNumber(dto.getNumber());
        card.setExpiry(dto.getExpiry());
        card.setBalance(dto.getBalance());
        card.setStatus(com.example.bankcards.entity.CardStatus.ACTIVE);
        card.setOwner(userService.findById(dto.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));
        CardDto result = CardDto.fromEntity(cardService.save(card));
        return result;
    }

    @GetMapping("/my")
    public Page<CardDto> myCards(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(required = false) String search) {
        String username = userService.getCurrentUser().orElseThrow(() -> new ResourceNotFoundException("User not found")).getUsername();
        Page<CardDto> result = cardService
                .findByOwner(username, PageRequest.of(page, size), search)
                .map(CardDto::fromEntity);
        return result;
    }

    @GetMapping("/{id}")
    public CardDto get(@PathVariable Long id) {
        User current = userService.getCurrentUser()
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        boolean admin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        Card card = admin
                ? cardService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Card not found"))
                : cardService.findByIdAndOwner(id, current.getUsername())
                        .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        return CardDto.fromEntity(card);
    }

    @PostMapping("/transfer")
    public void transfer(@Valid @RequestBody TransferDto dto) {
        String username = userService.getCurrentUser().orElseThrow(() -> new ResourceNotFoundException("User not found")).getUsername();
        Card from = cardService.findByIdAndOwner(dto.getFromId(), username)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        Card to = cardService.findByIdAndOwner(dto.getToId(), username)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        cardService.transfer(from, to, dto.getAmount());
    }

    @PostMapping("/{id}/block")
    public CardDto block(@PathVariable Long id) {
        User current = userService.getCurrentUser()
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        boolean admin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        Card card = admin
                ? cardService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Card not found"))
                : cardService.findByIdAndOwner(id, current.getUsername())
                        .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        CardDto result = CardDto.fromEntity(cardService.blockCard(card));
        return result;
    }

    @GetMapping("/{id}/balance")
    public AmountDto balance(@PathVariable Long id) {
        String username = userService.getCurrentUser().orElseThrow(() -> new ResourceNotFoundException("User not found")).getUsername();
        Card card = cardService.findByIdAndOwner(id, username)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        AmountDto dto = new AmountDto();
        dto.setAmount(card.getBalance());
        return dto;
    }

    @PostMapping("/{id}/deposit")
    public CardDto deposit(@PathVariable Long id, @Valid @RequestBody AmountDto dto) {
        String username = userService.getCurrentUser().orElseThrow(() -> new ResourceNotFoundException("User not found")).getUsername();
        Card card = cardService.findByIdAndOwner(id, username)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        CardDto result = CardDto.fromEntity(cardService.deposit(card, dto.getAmount()));
        return result;
    }

    @PostMapping("/{id}/withdraw")
    public CardDto withdraw(@PathVariable Long id, @Valid @RequestBody AmountDto dto) {
        String username = userService.getCurrentUser().orElseThrow(() -> new ResourceNotFoundException("User not found")).getUsername();
        Card card = cardService.findByIdAndOwner(id, username)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        CardDto result = CardDto.fromEntity(cardService.withdraw(card, dto.getAmount()));
        return result;
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CardDto activate(@PathVariable Long id) {
        Card card = cardService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        CardDto result = CardDto.fromEntity(cardService.activateCard(card));
        return result;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(@PathVariable Long id) {
        cardService.delete(id);
    }
}
