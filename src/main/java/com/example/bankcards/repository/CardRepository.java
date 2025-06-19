package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for performing CRUD operations on {@link Card} entities.
 */

public interface CardRepository extends JpaRepository<Card, Long> {
}
