package com.example.bankcards.controller;

import com.example.bankcards.dto.AmountDto;
import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.TransferDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @GetMapping("/my")
    public Page<CardDto> myCards(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        String username = userService.getCurrentUser().orElseThrow(() -> new ResourceNotFoundException("User not found")).getUsername();
        return cardService.findByOwner(username, PageRequest.of(page, size))
                .map(CardDto::fromEntity);
    }

    @GetMapping("/{id}")
    public CardDto get(@PathVariable Long id) {
        String username = userService.getCurrentUser().orElseThrow(() -> new ResourceNotFoundException("User not found")).getUsername();
        Card card = cardService.findByIdAndOwner(id, username)
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
        String username = userService.getCurrentUser().orElseThrow(() -> new ResourceNotFoundException("User not found")).getUsername();
        Card card = cardService.findByIdAndOwner(id, username)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        return CardDto.fromEntity(cardService.blockCard(card));
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
        return CardDto.fromEntity(cardService.deposit(card, dto.getAmount()));
    }

    @PostMapping("/{id}/withdraw")
    public CardDto withdraw(@PathVariable Long id, @Valid @RequestBody AmountDto dto) {
        String username = userService.getCurrentUser().orElseThrow(() -> new ResourceNotFoundException("User not found")).getUsername();
        Card card = cardService.findByIdAndOwner(id, username)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        return CardDto.fromEntity(cardService.withdraw(card, dto.getAmount()));
    }
}
