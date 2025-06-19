package com.example.bankcards.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardMaskUtilTest {

    @Test
    void maskReturnsMaskedNumber() {
        String masked = CardMaskUtil.mask("1234567812345678");
        assertEquals("**** **** **** 5678", masked);
    }

    @Test
    void maskReturnsOriginalWhenTooShort() {
        assertEquals("123", CardMaskUtil.mask("123"));
    }
}
