package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для выполнения CRUD-операций над сущностями {@link Card}.
 */

public interface CardRepository extends JpaRepository<Card, Long> {
}
